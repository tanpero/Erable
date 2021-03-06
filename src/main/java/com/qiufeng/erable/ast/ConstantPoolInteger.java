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

package com.qiufeng.erable.ast;

import com.qiufeng.erable.OpCode;
import com.qiufeng.erable.util.BitUtils;
import java.io.IOException;

/**
 * @since 2019年4月16日
 * @author Qiufeng54321
 */
public class ConstantPoolInteger extends ConstantPoolElement{

    public ConstantPoolInteger(Integer obj) {
	super(obj);
    }


    /**
     * Generated the header
     */
    public void generateHeader()throws IOException {
	this.writeOpCode(OpCode.CP_INT);
    }

    @Override
    public void write() throws IOException {
	this.generateHeader();
	byte[] doub=new byte[4];
	BitUtils.putInt(doub,0,(int)obj);
	this.file.write(doub);
    }

}
