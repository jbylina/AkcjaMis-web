package org.akcjamis.webapp.domain.util;

import org.hibernate.spatial.dialect.h2geodb.GeoDBDialect;

import java.sql.Types;

public class FixedH2Dialect extends GeoDBDialect {

    public FixedH2Dialect() {
        super();
        registerColumnType(Types.FLOAT, "real");
    }
}
