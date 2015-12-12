package org.dialect;
/*
 * The author disclaims copyright to this source code.  In place of
 * a legal notice, here is a blessing:
 *
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 *
 */

import org.hibernate.JDBCException;
import org.hibernate.ScrollMode;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.function.*;
import org.hibernate.dialect.pagination.AbstractLimitHandler;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.dialect.pagination.LimitHelper;
import org.hibernate.dialect.unique.DefaultUniqueDelegate;
import org.hibernate.dialect.unique.UniqueDelegate;
import org.hibernate.engine.spi.RowSelection;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.LockAcquisitionException;
import org.hibernate.exception.spi.SQLExceptionConversionDelegate;
import org.hibernate.exception.spi.TemplatedViolatedConstraintNameExtracter;
import org.hibernate.exception.spi.ViolatedConstraintNameExtracter;
import org.hibernate.internal.util.JdbcExceptionHelper;
import org.hibernate.mapping.Column;
import org.hibernate.type.StandardBasicTypes;

import java.sql.SQLException;
import java.sql.Types;

public class SQLiteDialect extends Dialect {
	private static final AbstractLimitHandler LIMIT_HANDLER = new AbstractLimitHandler() {
		@Override
		public String processSql(String sql, RowSelection selection) {
			final boolean hasOffset = LimitHelper.hasFirstRow(selection);
			return sql + (hasOffset ? " limit ? offset ?" : " limit ?");
		}

		@Override
		public boolean supportsLimit() {
			return true;
		}

		@Override
		public boolean bindLimitParametersInReverseOrder() {
			return true;
		}
	};
	private static final int SQLITE_BUSY = 5;

	// IDENTITY support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private static final int SQLITE_LOCKED = 6;
	private static final int SQLITE_IOERR = 10;
	private static final int SQLITE_CORRUPT = 11;
	private static final int SQLITE_NOTFOUND = 12;

	// GUID support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private static final int SQLITE_FULL = 13;

	// limit/offset support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private static final int SQLITE_CANTOPEN = 14;
	private static final int SQLITE_PROTOCOL = 15;

	// lock acquisition support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private static final int SQLITE_TOOBIG = 18;
	private static final int SQLITE_CONSTRAINT = 19;
	private static final int SQLITE_MISMATCH = 20;

	// current timestamp support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private static final int SQLITE_NOTADB = 26;
	private static final ViolatedConstraintNameExtracter EXTRACTER = new TemplatedViolatedConstraintNameExtracter() {
		@Override
		protected String doExtractConstraintName(SQLException sqle) throws NumberFormatException {
			final int errorCode = JdbcExceptionHelper.extractErrorCode(sqle);
			if (errorCode == SQLITE_CONSTRAINT) {
				return extractUsingTemplate("constraint ", " failed", sqle.getMessage());
			}
			return null;
		}
	};
	private final UniqueDelegate uniqueDelegate;

	// SQLException support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public SQLiteDialect() {
		registerColumnType(Types.BIT, "boolean");
		registerColumnType(Types.INTEGER, "integer");
		registerColumnType(Types.REAL, "real");
		registerColumnType(Types.DECIMAL, "decimal");
		registerColumnType(Types.CHAR, "char");
		registerColumnType(Types.VARCHAR, "varchar($l)");
		registerColumnType(Types.LONGVARCHAR, "longvarchar");
		registerColumnType(Types.TIMESTAMP, "datetime");
		registerColumnType(Types.BINARY, "blob");
		registerColumnType(Types.VARBINARY, "blob");
		registerColumnType(Types.LONGVARBINARY, "blob");
		registerFunction("concat", new VarArgsSQLFunction(StandardBasicTypes.STRING, "", "||", ""));
		registerFunction("mod", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "?1 % ?2"));
		registerFunction("quote", new StandardSQLFunction("quote", StandardBasicTypes.STRING));
		registerFunction("random", new NoArgSQLFunction("random", StandardBasicTypes.INTEGER));
		registerFunction("round", new StandardSQLFunction("round"));
		registerFunction("substr", new StandardSQLFunction("substr", StandardBasicTypes.STRING));
		registerFunction("trim", new AbstractAnsiTrimEmulationFunction() {
			protected SQLFunction resolveBothSpaceTrimFunction() {
				return new SQLFunctionTemplate(StandardBasicTypes.STRING, "trim(?1)");
			}

			protected SQLFunction resolveBothSpaceTrimFromFunction() {
				return new SQLFunctionTemplate(StandardBasicTypes.STRING, "trim(?2)");
			}

			protected SQLFunction resolveLeadingSpaceTrimFunction() {
				return new SQLFunctionTemplate(StandardBasicTypes.STRING, "ltrim(?1)");
			}

			protected SQLFunction resolveTrailingSpaceTrimFunction() {
				return new SQLFunctionTemplate(StandardBasicTypes.STRING, "rtrim(?1)");
			}

			protected SQLFunction resolveBothTrimFunction() {
				return new SQLFunctionTemplate(StandardBasicTypes.STRING, "trim(?1, ?2)");
			}

			protected SQLFunction resolveLeadingTrimFunction() {
				return new SQLFunctionTemplate(StandardBasicTypes.STRING, "ltrim(?1, ?2)");
			}

			protected SQLFunction resolveTrailingTrimFunction() {
				return new SQLFunctionTemplate(StandardBasicTypes.STRING, "rtrim(?1, ?2)");
			}
		});
		uniqueDelegate = new SQLiteUniqueDelegate(this);
	}

	@Override
	public boolean supportsIdentityColumns() {
		return true;
	}

	@Override
	public boolean hasDataTypeInIdentityColumn() {
		return false; // As specified in NHibernate dialect
	}

	@Override
	public String getIdentityColumnString() {
		return "integer";
	}

	@Override
	public String getIdentitySelectString() {
		return "select last_insert_rowid()";
	}

	@Override
	public String getSelectGUIDString() {
		return "select hex(randomblob(16))";
	}

	@Override
	public LimitHandler getLimitHandler() {
		return LIMIT_HANDLER;
	}

	@Override
	public boolean supportsLockTimeouts() {
		return false;
	}

	@Override
	public String getForUpdateString() {
		return "";
	}

	@Override
	public boolean supportsOuterJoinForUpdate() {
		return false;
	}

	@Override
	public boolean supportsCurrentTimestampSelection() {
		return true;
	}

	public boolean isCurrentTimestampSelectStringCallable() {
		return false;
	}

	@Override
	public String getCurrentTimestampSelectString() {
		return "select current_timestamp";
	}

	@Override
	public SQLExceptionConversionDelegate buildSQLExceptionConversionDelegate() {
		return new SQLExceptionConversionDelegate() {
			@Override
			public JDBCException convert(SQLException sqlException, String message, String sql) {
				final int errorCode = JdbcExceptionHelper.extractErrorCode(sqlException);
				if (errorCode == SQLITE_TOOBIG || errorCode == SQLITE_MISMATCH) {
					return new DataException(message, sqlException, sql);
				} else if (errorCode == SQLITE_BUSY || errorCode == SQLITE_LOCKED) {
					return new LockAcquisitionException(message, sqlException, sql);
				} else if ((errorCode >= SQLITE_IOERR && errorCode <= SQLITE_PROTOCOL) || errorCode == SQLITE_NOTADB) {
					return new JDBCConnectionException(message, sqlException, sql);
				}
				return null;
			}
		};
	}

	public ViolatedConstraintNameExtracter getViolatedConstraintNameExtracter() {
		return EXTRACTER;
	}

	// union subclass support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@Override
	public boolean supportsUnionAll() {
		return true;
	}

	// miscellaneous suppoer ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@Override
	public String getNoColumnsInsertString() {
		return "default values";
	}

	// DDL support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@Override
	public boolean canCreateSchema() {
		return false;
	}

	@Override
	public boolean dropConstraints() {
		return false;
	}

	@Override
	public boolean qualifyIndexName() {
		return false;
	}

	@Override
	public boolean hasAlterTable() {
		return false;
	}

	@Override
	public String getAddColumnString() {
		return "add column";
	}

	@Override
	public String getDropForeignKeyString() {
		throw new UnsupportedOperationException("No drop foreign key syntax supported by SQLiteDialect");
	}

	@Override
	public String getAddForeignKeyConstraintString(String constraintName, String[] foreignKey, String referencedTable, String[] primaryKey, boolean referencesPrimaryKey) {
		throw new UnsupportedOperationException("No add foreign key syntax supported by SQLiteDialect");
	}

	@Override
	public String getAddPrimaryKeyConstraintString(String constraintName) {
		throw new UnsupportedOperationException("No add primary key syntax supported by SQLiteDialect");
	}

	@Override
	public boolean supportsCommentOn() {
		return true;
	}

	@Override
	public String getTableComment(String comment) {
		return comment;
	}

	@Override
	public String getColumnComment(String comment) {
		return comment;
	}

	@Override
	public boolean supportsIfExistsBeforeTableName() {
		return true;
	}

	@Override
	public boolean doesReadCommittedCauseWritersToBlockReaders() {
		return true;
	}

	public boolean doesRepeatableReadCauseReadersToBlockWriters() {
		return true;
	}

	@Override
	public boolean supportsTupleDistinctCounts() {
		return false;
	}

	public int getInExpressionCountLimit() {
		return 1000;
	}

	@Override
	public UniqueDelegate getUniqueDelegate() {
		return uniqueDelegate;
	}

	@Override
	public ScrollMode defaultScrollMode() {
		return ScrollMode.FORWARD_ONLY;
	}

	private static class SQLiteUniqueDelegate extends DefaultUniqueDelegate {
		public SQLiteUniqueDelegate(Dialect dialect) {
			super(dialect);
		}

		@Override
		public String getColumnDefinitionUniquenessFragment(Column column) {
			return " unique";
		}
	}
}
