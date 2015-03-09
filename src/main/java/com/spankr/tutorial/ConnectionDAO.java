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

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * Let's create a simple pooled datasource for Mr. Fultz
 *
 * @author Lee_Vettleson
 *
 */
public class ConnectionDAO {

	/**
	 * Git me mah datasource!
	 * 
	 * @return datasource pointing at the DEV version of partsearch
	 */
	public DataSource getDataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
		ds.setUrl("jdbc:hsqldb:mem:sampleDB");
		ds.setUsername("SA");
		ds.setPassword("");
		ds.setInitialSize(2);
		ds.setMaxActive(20);
		return ds;
	}
}
