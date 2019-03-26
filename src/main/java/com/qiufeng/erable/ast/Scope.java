package com.qiufeng.erable.ast;
import java.util.*;

/**
 * @since 23 March 2019
 * @version 1.0
 * @author Qiufeng54321
 *
 */
public class Scope {
	public static HashSet<Object> idTable=new HashSet<>();
	static {
		Scope.addObject("null");
		Scope.addObject("true");
		Scope.addObject("false");
	}
	ArrayList<Code> codes=new ArrayList<>();
	Scope parent;
	public static enum Type{
		FUNCTION,
		VARIABLE,
		CODEBLOCK,
		IF;
	}
	public Type type;
	public Scope(Scope p,Type t) {
		parent=p;
		type=t;
		//System.out.println("current depth:"+cdepth);
		
	}
	public Scope getParent() {
		return parent;
	}
	public Scope getRoot() {
		Scope rt=this;
		while(rt.parent!=null) {
			rt=rt.getParent();
		}
		return rt;
	}
	public Scope createChild(Type t) {
		return new Scope(this,t);
	}
	public int declareFunction(int name,int[] args,Scope code) {
		FuncDeclCode fdc=new FuncDeclCode(name,args,code);
		addCode(fdc);
		return fdc.id;
	}
	public int declareVariable(int id,int value,boolean isArgs) {
		VarCode c=new VarCode(id,value,isArgs);
		addCode(c);
		return c.id;
	}
	public int declareVariable(int id,int value) {
		return this.declareVariable(id, value,false);
	}
	public void addCode(Code c) {
		codes.add(c);
	}
	public int findConstByTemp(int tempid) {
		System.out.println("Finding const by temp: "+tempid);
		
		for(Code me : codes) {
			if(me instanceof TempCode) {
				TempCode tc=(TempCode)me;
				//System.out.println("Iterating reference ids:"+tc.refid+", tmp id:"+tc.id);
				if(tempid==tc.id) {
					System.out.println("Found id:"+tc.refid);
					return tc.refid;
				}
			}
		}
		if(parent!=null) {
			return parent.findConstByTemp(tempid);
		}
		return -1;
	}
	public int findTempByConst(int constid) {
		System.out.println("Finding temp by const: "+constid);
		
		for(Code me : codes) {
			if(me instanceof TempCode) {
				TempCode tc=(TempCode)me;
				//System.out.println("Iterating reference ids:"+tc.refid+", tmp id:"+tc.id);
				if(constid==tc.refid) {
					System.out.println("Found id:"+tc.id);
					return tc.id;
				}
			}
		}
		if(parent!=null) {
			return parent.findTempByConst(constid);
		}
		return -1;
	}
	/**
	 * Find variable/function id for the given name id(temp id).<br/>
	 * If <strong>Not Found</strong>,return -1;
	 * @param id temp id of the name
	 * @return the variable/function id
	 */
	public int findTempExists(int id) {
		System.out.println("Finding temp exists: "+id);
		
		for(Code me : codes) {
			/**
			 * Can only copy the code twice because <code>VarCode</code> and <code>FuncDeclCode</code>
			 * are different.
			 */
			if(me instanceof VarCode) {
				VarCode tc=(VarCode)me;
				int cid=this.findConstByTemp(tc.refid);
				//System.out.println("Iterating var reference ids:"+tc.refid+", tmp id:"+tc.id);
				if(id==cid) {
					System.out.println("Found var id:"+tc.id);
					return tc.id;
				}
			}else if(me instanceof FuncDeclCode) {
				FuncDeclCode tc=(FuncDeclCode)me;
				int cid=this.findConstByTemp(tc.refid);
				//System.out.println("Iterating funcdecl reference ids:"+tc.refid+", tmp id:"+tc.id);
				if(id==cid) {
					System.out.println("Found funcdecl id:"+tc.id);
					return tc.id;
				}
			}
		}
		if(parent!=null) {
			return parent.findTempExists(id);
		}
		return -1;
	}
	/**
	 * Create a temp for <em>constant value</em>(Buffer).</br>
	 * If the value is a <strong>value/function</strong>,return an existed <strong>variable/function</strong> id pointer.
	 * @param id constant id
	 * @return {@link Integer}: temp id created.
	 */
	public int temp(int id) {
		int varid=findTempExists(id);
		if(varid!=-1)return varid;
		TempCode tc=new TempCode(id);
		addCode(tc);
		return tc.id;
	}
	public int findFunction(int id) {
		for(Code me : codes) {
			if(! (me instanceof FuncDeclCode))continue;
			FuncDeclCode trans=(FuncDeclCode)me;
			if(trans.id==id) {
				return trans.id;
			}
		}
		if(parent!=null) {
			return parent.findFunction(id);
		}
		return -1;
	}
	public int findVariable(int name) {
		for(Code me : codes) {
			if(!(me instanceof VarCode))continue;
			VarCode trans=(VarCode)me;
			if(trans.id==name) {
				return trans.id;
			}
		}
		if(parent!=null) {
			return parent.findVariable(name);
		}
		return -1;
	}
	/**
	 * @param id
	 * @return Scope
	 */
	public int find(int id) {
		int ret=findFunction(id);
		if(ret==-1)
		  ret=findVariable(id);
		System.out.println("result finding var/func id "+ id+ "="+ ret);
		return ret;
	}
	public static int addObject(Object o) {
		Scope.idTable.add(o);
		return findId(o);
	}
	/**
	 * Find the object stored in constant pool.
	 * @param id constant pool id.
	 * @return {@link Object} the object found in constant pool.If not found, return <code>null</code>.
	 */
	public static Object findObject(int id) {
		Object idb=null;
		Iterator<?> it=Scope.idTable.iterator();
		for(int i=0;i<id;idb=it.next());
		return idb;
	}
	/**
	 * Find id of the object in constant pool.
	 * @param obj value from constant pool.
	 * @return {@link Integer} the constant pool id found.If not found, return <code>-1</code>.
	 */
	public static int findId(Object obj) {
		try {
			Object idb;
		Iterator<?> it=Scope.idTable.iterator();
		for(int i=0;i<Scope.idTable.size();i++) {
			idb=it.next();
			if(idb.equals(obj))return i;
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	/**
	 * For diagnostic and debug.
	 * @return {@link String}: tree structure parsed.
	 */
	public String tree() {
		String res="-----------ID Table-----------\n";
		String prefix="->";
		int cid=0;
		for(Object me : idTable) {
			String out=prefix;
			out+=me;
			out+="        ID:";
			out+=cid;
			out+="\n";
			res+=out;
			cid++;
		}
		res+=treeVF(1);
		return res;
	}
	/**
	 * A simple wrapper for <code>System.out.println(tree());</code>
	 */
	public void printTree() {
		System.out.println(tree());
	}
	/**
	 * tree-ify the current scope.<br/>
	 * calls {@link com.qiufeng.erable.ast.Code#toString()}.<br/>
	 * returns the codes declared.
	 * @param depth
	 * @return {@link String}: the string which contains stringified codes.
	 */
	String treeVF(int depth) {
		String prefix="-".repeat(depth)+">";
		String res="";
		res+=prefix;
		res+="CODES-----\n";
		for(Code me : codes) {
			res+=prefix;
			res+=me.toString();
			res+="\n";
		}
		return res;
	}
	@Override
	public String toString() {
		return treeVF(4);
	}
	
}
