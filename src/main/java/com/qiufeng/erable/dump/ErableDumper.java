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

package com.qiufeng.erable.dump;

import com.qiufeng.erable.Const;
import com.qiufeng.erable.OpCode;
import com.qiufeng.erable.util.BitUtils;
import com.qiufeng.erable.util.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @since 2019年4月9日
 * @author Qiufeng54321
 */
public class ErableDumper {
    private static final Logger LOG = Logger.getLogger(ErableDumper.class.getName());
    public InputStream input;
    public int depth=0;
    public int id_len=1;
    public int cid_len=1;
    public int line=0;

    public InputStream getInput() {
	return input;
    }

    public void setInput(InputStream input) {
	this.input = input;
    }

    public ErableDumper(InputStream input) {
	this.input = input;
    }
    public String depthHeader(){
	return "-".repeat(depth)+">";
    }
    public void dump(OutputStream out) throws IOException{
	PrintStream ps=new PrintStream(out);
	byte[] magic=input.readNBytes(2);
	if(!(magic[0]==Const.MAGIC[0]&&magic[1]==Const.MAGIC[1])){
	    LOG.log(Level.SEVERE, "Magic not correct: {0}, Expecting: {1}", new Object[]{Arrays.toString(magic),Arrays.toString(Const.MAGIC)});
	    System.exit(2);
	}
	byte[] version=input.readNBytes(2);
	System.out.println("Major version: "+version[0]+", Minor version: "+version[1]);
	byte   len    =(byte)input.read();
	id_len=len>>4;
	cid_len=len&0x01;
	System.out.println("ID Length: "+id_len+", CID Length: "+cid_len);
	//this.input.readNBytes(4);
	//this.dumpConstantPool();
	this.dumpCodes();
    }
    public void dumpDynLib(InputStream is)throws IOException{
	System.out.println("Dumping dynamic lib name table");
	Properties prop=new Properties();
	prop.load(is);
	for(Map.Entry me : prop.entrySet()){
	    String key=(String)me.getKey();
	    int id=Integer.parseInt((String)me.getValue());
	    System.out.println("Name \"" + key + "\" with ID " + id);
	}
    }
    public void dumpCodes() throws IOException{
	for(int i=0;(i=this.input.read())!=-1;){
	    System.out.print("#"+StringUtils.std(String.valueOf(line++), 8));
	    System.out.print(this.depthHeader());
	    
	    OpCode code=OpCode.values()[i];
	    //System.out.println(code);
	    
	    switch (code) {
	    	case LOADC:
		    {
			int cid=this.readId(cid_len);
			int id =this.readId(id_len);
			System.out.println("Buffer                    "+cid+" to @"+id);
			break;
		    }
	    	case DYNCALL:
		    {
			int mid=this.readId(id_len);
			int nid =this.readId(4);
			int id  =this.readId(id_len);
			System.out.println("Access                    @"+nid+" from Module @"+mid+" to @"+id);
			break;
		    }
		case CONSTANT_POOL:
		    this.dumpConstantPool();
		    break;
	    	default:
		    String name=StringUtils.std(code.toString(),25);
		    System.out.print(name);
		    for(int j=0;j<code.argc;j++){
			int id=this.readId(id_len);
			System.out.print(" @"+id);
		    }   System.out.println();
		    if(code==OpCode.PUSH_SCOPE||code==OpCode.IF||code==OpCode.ELSE||code==OpCode.WHILE||code==OpCode.FUNCTION
			    ||code==OpCode.TRY)
			this.depth+=2;
		    if(code==OpCode.POP_SCOPE||code==OpCode.END)
			this.depth-=2;
		    break;

	    }
	    
	    
	}
    }
    public void dumpConstantPool()throws IOException{
	int len=this.readId(cid_len);
	System.out.println("Constant Pool: Length="+len);
	this.depth+=2;
	for(int i=0;i<len;i++){
	    System.out.print("$"+StringUtils.std(String.valueOf(line++), 8));
	    System.out.print(this.depthHeader());
	    OpCode code=OpCode.values()[this.input.read()];
	    if(null!=code)switch (code) {
	    	case CP_NUM:{
		    byte[] doubb=this.input.readNBytes(8);
		    double doub=BitUtils.getDouble(doubb, 0);
		    System.out.println("Number : ID="+StringUtils.std(String.valueOf(i), 8)+", Value="+doub);
			break;
		    }
	    	case CP_INT:{
		    byte[] doubb=this.input.readNBytes(4);
		    int doub=BitUtils.getInt(doubb, 0);
		    System.out.println("Integer: ID="+StringUtils.std(String.valueOf(i), 8)+", Value="+doub);
			break;
		    }
	    	case CP_STR:
		    int strlen=this.readId(4);
		    String str=new String(this.input.readNBytes(strlen));
		    System.out.println("String : ID="+StringUtils.std(String.valueOf(i), 8)+", Value=\""+str+"\"");
		    break;
	    	default:
		    break;
	    }
	}
	this.depth-=2;
    }
    public int readId(int len) throws IOException{
	byte[] bs=this.input.readNBytes(len);
	return Const.getId(bs,0,len);
    }
}
