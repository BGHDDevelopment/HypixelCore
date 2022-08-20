/*
 * Copyright (c) BGHDDevelopment LLC 2021
 */

package net.bghd.hypixel.core.managers;

import java.sql.ResultSet;

public interface SelectCall {

    void call(ResultSet resultSet);
}
