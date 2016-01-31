# SpletneSeje #

Program za odelavo log datotek.

Uporabljena licenca [GPL V3](LICENSE.md)

## Help

 <path>                                 : Input log files
 -c (crawlers) <bool>                   : Ignore web rawlers
 -dbcp (database.connection.pool_size)  : Number of connetions alowed for
 <int>                                    connecting to database
 -dbddl (database.ddl) [Create |        : Create new tables or update exsisting
 Update]                                  ones
 -dbdi (database.dialect.class)         : Database dialect class name
 <string>
 -dbdic (database.dialect) <path>       : Path to class file, that is dialect
                                          for database
 -dbdr (database.driver) <path>         : Path to jar file, that is a driver
 -dbdrc (database.driver.class)         : Driver class for connecting to databse
 <string>
 -dbpw (database.password) <string>     : Password for user
 -dbsq (database.sql.show) <bool>       : Show sql querys
 -dbsqf (database.sql.show.format)      : Show formated sql querys
 <bool>
 -dbun (database.username) <string>     : User name for database
 -dburl (database.url) <URL>            : URL to database
 -ds (database.schema) <string>         : Default database schema
 -fd (format.date) <date format>        : Date format
 -fl (format.log) <log format>          : Log file format. Check NCSA or W3C
                                          log formats.
 -flo (format.locale) <tag>             : Locale for time parsing. Check
                                          lahguage tags.
 -ft (format.time) <time format>        : Time format
 -pp (objectpool.properties) <path>     : Properties for ObjectPool!!!
 -ps (parse.size) <int>                 : Number of sessions
 -sqps (session.queue.parsedline.size)  : Queuq size for ParsedLine class
 <int>
 -sqss (session.queue.session.size)     : Queue size for Session class
 <int>
 -st (session.time) <int>               : Time until session is over. Time is
                                          represented in seconds.
 li (log.ignore) <string>               : Fields to ignore
 -props <path>                          : Path to configuration file

Usage:

Process finished with exit code 1
ProgName <path> ... [-c (crawlers) <bool>] [-dbcp (database.connection.pool_size) <int>] [-dbddl (database.ddl) [Create | Update]] [-dbdi (database.dialect.class) <string>] [-dbdic (database.dialect) <path>] [-dbdr (database.driver) <path>] [-dbdrc (database.driver.class) <string>] [-dbpw (database.password) <string>] [-dbsq (database.sql.show) <bool>] [-dbsqf (database.sql.show.format) <bool>] [-dbun (database.username) <string>] [-dburl (database.url) <URL>] [-ds (database.schema) <string>] [-fd (format.date) <date format>] [-fl (format.log) <log format>] [-flo (format.locale) <tag>] [-ft (format.time) <time format>] [-h (--help, -?)] [-pp (objectpool.properties) <path>] [-ps (parse.size) <int>] [-sqps (session.queue.parsedline.size) <int>] [-sqss (session.queue.session.size) <int>] [-st (session.time) <int>] [li (log.ignore) <string>] [-props <path>]