/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.
    
    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.spankr.tutorial;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import junit.framework.Assert;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.DbUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Let's test our datasource!
 *
 * @author Lee_Vettleson
 *
 */
public class TestConnectionDAO {
	private static final Logger log = LoggerFactory.getLogger(TestConnectionDAO.class);

	private DataSource ds;

	ConnectionDAO daoConnector = new ConnectionDAO();

	/**
	 * Before every test runs, grab a datasource from da pool
	 */
	@Before
	public void setup() {
		ds = daoConnector.getDataSource();

		assertNotNull("DataSource can't be null, foo!", ds);
	}

	/**
	 * @throws SQLException
	 * 
	 */
	@Test
	public void getSomeData() throws SQLException {
		Connection con = null;

		try {
			printActiveConnections();
			con = ds.getConnection();
			printActiveConnections();

			Statement stmt = null;
			log.info("Creating sample_table");
			stmt = con.createStatement();
			stmt.execute("CREATE TABLE sample_table (id INT IDENTITY, first_name VARCHAR(30), last_name VARCHAR(30), age INT)");
			DbUtils.closeQuietly(stmt);

			PreparedStatement pstmt = null;
			log.info("Inserting a person into sample_table");
			pstmt = con.prepareStatement("INSERT INTO sample_table VALUES (null, ?, ?, ?)");
			pstmt.setString(1, "Bob");
			pstmt.setString(2, "Haskins");
			pstmt.setInt(3, 38);
			Assert.assertTrue(pstmt.executeUpdate() == 1); // success means exactly one row inserted
			DbUtils.closeQuietly(pstmt);

			log.info("Getting a count of rows in sample_table");
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT count(1) from sample_table");
			if (rs.next()) {
				long l = rs.getLong(1);
				assertTrue("Shouldn't get a zero count", l > 0);
				log.debug(String.format("Total records = %s", l));
			} else {
				fail("Nothing returned from the database query");
			}
		} catch (SQLException e) {
			fail("Unable to create the database table");
		} finally {
			DbUtils.closeQuietly(con);
			printActiveConnections();
		}
	}

	/**
	 * Print out how many active and idle connections currently exist in the datasource
	 */
	private void printActiveConnections() {
		BasicDataSource bds = (BasicDataSource) ds;
		log.debug(String.format(
				"Number of active/idle connections: %s/%s", bds.getNumActive(),
				bds.getNumIdle()));
	}
}
