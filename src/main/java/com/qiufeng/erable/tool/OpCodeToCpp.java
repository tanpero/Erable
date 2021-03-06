/*
 * Copyright (C) 2019 Qiufeng54321
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.qiufeng.erable.tool;

import com.qiufeng.erable.OpCode;

/**
 * @since 2019年4月20日
 * @author Qiufeng54321
 */
public class OpCodeToCpp {
    public static void main(String[] args) {
	for(OpCode oc : OpCode.values()){
	    System.out.println("this->addEnum(\"" + oc.name() + "\", " + oc.argc + ", " + oc.idIndex + ");	    // Index " + oc.ordinal());
	}
    }
}
