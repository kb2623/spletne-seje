/**
 * FailSQLJet.java
 * Copyright (C) 2009-2013 TMate Software Ltd
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * For information on how to redistribute this software under
 * the terms of a license other than GNU General Public License
 * contact TMate Software at support@sqljet.com
 */
package org.tmatesoft.sqljet.compatibility;

import junit.framework.Assert;

import org.junit.Test;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.junit.Ignore;

@Ignore("Connection ne deluje")
public class CompabilityLockingReadSQLJetBusy {
    
    @Test(expected = SqlJetException.class)
    public void readLockSQLJetBusy() throws SqlJetException {
        CompabilityLockingAbstract.readLockSQLJet();
        Assert.fail();
    }
    
}