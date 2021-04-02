import visitor.GJDepthFirst;
import syntaxtree.*;
import java.util.*;
import java.io.IOException;

import java.io.*;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;


public class SecondVisitor extends GJDepthFirst<String, String>{

	ArrayList<String> program=new ArrayList<String>();
	ArrayList<String> previousprogram=new ArrayList<String>();
	ArrayList<String> args=new ArrayList<String>();
	ArrayList<String> pars=new ArrayList<String>();
	String mainclass;
	int numregister=0;
	int ifregister=0;
	public void getArray(ArrayList<String> prog){
		//System.out.println("Second visitor:"+prog);
		previousprogram=prog;
	}
	
	public static boolean isNumeric(String str){
		for (char c : str.toCharArray()){
		    if (!Character.isDigit(c)) return false;
		}
		return true;
	}
	
	public boolean isavariable(String var){
		int index=0;
		while(index!=previousprogram.size()-2){
			if(previousprogram.get(index)==var){
				if(previousprogram.get(index-1)=="int"||previousprogram.get(index-1)=="boolean"){//tote einai variable
					return true;
				}
			}
			index++;
		}
		return false;
	}

	public boolean checkifitsavariableofclass(String var){
		int index=program.size()-1;
		while(program.get(index)!="class"){//pame pisw sth klash
			index--;
	    }
		while(program.get(index)!="public"&&index!=program.size()-2){
			if(program.get(index)==var){
				return true;
			}
			index++;
		}
		return false;
	}


	public String superclassesforvariables(String Superclass,String variable){
		
		int size=previousprogram.size()-1;
		int index=0;
		int flag=0;
		String type;
		while(index!=size){

			if(previousprogram.get(index)=="class"&&previousprogram.get(index+1)==Superclass){//an brethike h superclass
				//System.out.println("Class "+Superclass+" variable "+variable);
				if(previousprogram.get(index+2)=="extends"){
					type=superclassesforvariables(previousprogram.get(index+3),variable);
					if(type!=";"){//an brethike o typos
						return type;
					}
					else{
						while(previousprogram.get(index)!="public"){
							if(previousprogram.get(index)==variable){
								if(previousprogram.get(index+2)=="new"){
									type=previousprogram.get(index+3);//apothikeysh typoy
									flag=1;
								}
								else{
									type=previousprogram.get(index-1);//apothikeysh toy typou
									flag=1;
									
								}
								break;
							}
							index++;
						}
						if(flag==0){//an de brethike se ayth th klash
							return ";";
						}
						else{//ama brethike
							return type;
						}
					}
				}
				else{//den exoume allh yperklash
					//System.out.println("gt");
					while(previousprogram.get(index)!="public"&&index!=previousprogram.size()-1){//diavazoume tis metablhtes ths klashs
						if(previousprogram.get(index)==variable){
							flag=1;//brethike
							break;
						}
						index++;
					}
					//System.out.println("perna");
					if(flag==0){
						return ";";//ayto gia keno
					}
					else{
						//System.out.println("TYPE "+previousprogram.get(index-1));
						if(previousprogram.get(index+2)=="new"){
							return previousprogram.get(index+3);
						}
						else{
							return previousprogram.get(index-1);//epistrefw ton typo
						}
					}
				}
				
			}
			index++;
		}
		return ";";
	}

	public String superclassesforfunctions(String superclass,String function){
		int index=0;
		//System.out.println("CLASS "+superclass+" FUNCTION "+function);
		while(index!=previousprogram.size()-1){
			//System.out.println("MPAINEI");
			if(previousprogram.get(index)=="class"&&previousprogram.get(index+1)==superclass){//an brethike h katallhlh klash
				
				if(previousprogram.get(index+2)=="extends"){//an exei yperklash
					String typeoffun=superclassesforfunctions(previousprogram.get(index+3),function);
					if(typeoffun==";"){//de brethike h synarthsh
						int flagfun=0;
						index++;
						while(index!=previousprogram.size()-1&&previousprogram.get(index)!="class"){//mexri na diavasoume tis synarthsheis ths klashs
							if(previousprogram.get(index)=="public"&&previousprogram.get(index+2)==function){
								typeoffun=previousprogram.get(index+1);//apothikeysh typou
								flagfun=1;
								break;
							}
							index++;
						}
						if(flagfun==0){
							return ";";
						}
						else{//an brethike h synarthsh
							return typeoffun;
						}
					}
					else{//brethike
						return typeoffun;
					}
				}
				else{
					String typeoffun=" ";
					index++;
					
					while(index!=previousprogram.size()&&previousprogram.get(index)!="class"){
						if(previousprogram.get(index)=="public"&&previousprogram.get(index+2)==function){
							typeoffun=previousprogram.get(index+1);
							
							return typeoffun;
						}
						index++;
					}
					return ";";//an de bethike h synarthsh
				}
			}
			index++;
		}
		return ";";
	}





	public String visit(NodeToken n, String argu) { return n.toString(); }
	
    /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> "public"
    * f4 -> "static"
    * f5 -> "void"
    * f6 -> "main"
    * f7 -> "("
    * f8 -> "String"
    * f9 -> "["
    * f10 -> "]"
    * f11 -> Identifier()
    * f12 -> ")"
    * f13 -> "{"
    * f14 -> ( VarDeclaration() )*
    * f15 -> ( Statement() )*
    * f16 -> "}"
    * f17 -> "}"
    */
   public String visit(MainClass n, String argu) {
	  int index=3;
	  int offsets=8;
	  int numoffuns=0;
	  String funname=null;
	  while(index!=previousprogram.size()-2){////edw ama exei polles klaseis de doyleyei
		if(previousprogram.get(index)=="class"){
			int index2=index+1;
			while(index2!=previousprogram.size()-2){
				if(previousprogram.get(index2)=="int"){
					offsets+=4;
				}
				else if(previousprogram.get(index2)=="boolean"){
					offsets+=1;
				}
				else if(previousprogram.get(index2)=="int[]"||previousprogram.get(index2)=="boolean[]"){
					offsets+=8;
				}
				else if(previousprogram.get(index2)=="public"){//teliwsame me tis metablites
					funname=previousprogram.get(index2+2);
					break;
				}
				//edw gia antikeimena ena if prepei
				index2++;
			}
			break;
		}
		
		index++;
 	  }
		
	  String classname=null;
	  index=3;
	  while(previousprogram.get(index)!="class"){
		index++;
	  }
	  classname=previousprogram.get(index+1);
	  while(index!=previousprogram.size()-2){
		if(previousprogram.get(index)=="public"){
			numoffuns+=1;
		}
		index++;
	  }



	  String filename=previousprogram.get(1)+".ll";
	  BufferedWriter bw=null;
	  try{
		  File f=new File(filename);
		  /*if(!file.exists()){
			file.createNewFile();
		  }*/
		  FileWriter fw=new FileWriter(f,true);
				
		  bw=new BufferedWriter(fw);
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

	  try{
	  	bw.write("define i32 @main() {\n");
		bw.write("%_"+numregister+" = call i8* @calloc(i32 1,i32 "+offsets+")\n");
		numregister++;
		bw.write("%_"+numregister+" = bitcast i8* %_0 to i8***\n");
		numregister++;
	  	bw.write("%_"+numregister+" = getelementptr ["+numoffuns+" x i8*], ["+numoffuns+" x i8*]* @."+classname+"_vtable, i32 0, i32 0\n");
		numregister++;
		bw.write("store i8** %_2, i8*** %_1\n");


		bw.write("%_"+numregister+" = bitcast i8* %_0 to i8***\n");
		numregister++;
		
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }
	  String _ret=null;
	  String name,klash;
	  n.f0.accept(this, argu);
	  program.add("class");
      klash=n.f1.accept(this, "main");
	  mainclass=klash;
	  program.add("{");
	  program.add("public");
	  program.add("static");
	  program.add("void");
	  program.add("main");
	  program.add("(");
	  program.add("String[]");
      n.f11.accept(this, "main");
      n.f14.accept(this, "main");
      n.f15.accept(this, "main");
      program.add("}");
	  program.add("}");
	  
	  try{
		//bw.write("call void (i32) @print_int(i32 %_8)\n");
		bw.write("ret i32 0\n}\n");
		bw.flush();
	  	bw.close();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }
	  
      return _ret;
   }

   /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
   */
   public String visit(TypeDeclaration n, String argu) {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
   public String visit(ClassDeclaration n, String argu) throws RuntimeException{
      String _ret=null;
	  String name;
      n.f0.accept(this, argu);
	  program.add("class");
      n.f1.accept(this, null);
	  name=program.get(program.size()-1);
      n.f2.accept(this, argu);
	  program.add("{");
      n.f3.accept(this, name);
      n.f4.accept(this, name);
      n.f5.accept(this, argu);
	  program.add("}");
      return _ret;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "extends"
    * f3 -> Identifier()
    * f4 -> "{"
    * f5 -> ( VarDeclaration() )*
    * f6 -> ( MethodDeclaration() )*
    * f7 -> "}"
    */
   public String visit(ClassExtendsDeclaration n, String argu) {
      String _ret=null;
	  String name;
      n.f0.accept(this, argu);
	  program.add("class");
      name=n.f1.accept(this, argu);
	  
      n.f2.accept(this, argu);
	  program.add("extends");
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
	  program.add("{");
      n.f5.accept(this, name);
      n.f6.accept(this, name);
      n.f7.accept(this, argu);
	  program.add("}");
      return _ret;
   }


   /**
    * f0 -> "public"
    * f1 -> Type()
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( FormalParameterList() )?
    * f5 -> ")"
    * f6 -> "{"
    * f7 -> ( VarDeclaration() )*
    * f8 -> ( Statement() )*
    * f9 -> "return"
    * f10 -> Expression()
    * f11 -> ";"
    * f12 -> "}"
    */
   public String visit(MethodDeclaration n, String argu) {//////////////////////////////////////////////////////
      String _ret=null;
	  String classname=argu;
	  
	  String filename=previousprogram.get(1)+".ll";
	  BufferedWriter bw=null;
	  try{
		  File f=new File(filename);
		  /*if(!file.exists()){
			file.createNewFile();
		  }*/
		  FileWriter fw=new FileWriter(f,true);
				
		  bw=new BufferedWriter(fw);
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

	  

	  String type;
      n.f0.accept(this, argu);
	  program.add("public");
      type=n.f1.accept(this, argu);	
	  //System.out.println("OLO TO PROGRAMA MEXRI EDW "+program);
      n.f2.accept(this, argu);
	  String namefun=program.get(program.size()-1);
	  //System.out.println("CLASS "+type);
	  try{
		bw.write("define ");
		//System.out.println("EDWWWWWWW "+type);
		if(type=="int"){
			bw.write("i32 ");
		}
		else if(type=="boolean"){
			bw.write("i1 ");
		}
		bw.write("@"+classname+"."+namefun+"(i8* %this");
		int index=0;
		while(index!=previousprogram.size()-2){
			if(previousprogram.get(index)=="public"&&previousprogram.get(index+2)==namefun){
				int index2=index+4;
				while(previousprogram.get(index2)!=")"){
					
					if(previousprogram.get(index2)=="int"){
						bw.write(", i32 %."+previousprogram.get(index2+1));
					}
					else if(previousprogram.get(index2)=="boolean"){
						bw.write(", i1 %."+previousprogram.get(index2+1));
					}
					index2++;
				}
				bw.write(") {\n");
				break;
			}
			index++;
		}
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }


	  //System.out.println("FUUUUUUn "+namefun);
      n.f3.accept(this, argu);
	  program.add("(");
	  n.f4.accept(this, argu);
	  
	  n.f5.accept(this, argu);
	  program.add(")");
	  int index=program.size()-1;
	  String klash=" ";
	  while(index!=0){
		if(program.get(index)=="class"){
			klash=program.get(index+1);
			break;
		}
		index--;
	  }
	  //System.out.println("EDWWWW "+type);
	  //checkfordoublefunctiondeclarations(argu,name);
	  n.f6.accept(this, argu);
	  program.add("{");
	  n.f7.accept(this, argu);
	  n.f8.accept(this, argu);
	  n.f9.accept(this, argu);
	  program.add("return");
	  String typeofreturn=n.f10.accept(this, argu);
	  try{
		if(isNumeric(program.get(program.size()-1))){
			bw.write("ret i32 "+program.get(program.size()-1)+"\n");
		}
		else{
			if(typeofreturn=="int"){
				bw.write("ret i32 %_"+(numregister-1)+"\n");
				bw.flush();
					
			}
		}
		bw.flush();
	  }
	  catch (IOException e) {
	      e.printStackTrace();
	  }
	  //System.out.println("AAAAAAAAAAAAA"+typeofreturn);
	  n.f11.accept(this, argu);
	  program.add(";");
	  n.f12.accept(this, argu);
	  program.add("}");
	  try{
		bw.write("}\n");
		bw.flush();
	  }
	  catch (IOException e) {
	      e.printStackTrace();
	  }
	
      return _ret;
   }


   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public String visit(VarDeclaration n, String argu) throws RuntimeException{
      String _ret=null;

	  String filename=previousprogram.get(1)+".ll";
	  BufferedWriter bw=null;
	  try{
		  File f=new File(filename);
		  /*if(!file.exists()){
			file.createNewFile();
		  }*/
		  FileWriter fw=new FileWriter(f,true);
				
		  bw=new BufferedWriter(fw);
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }



      String type=n.f0.accept(this, argu);
      String name=n.f1.accept(this, argu);
	  //System.out.println("TYPE and NAME="+type+name);

      n.f2.accept(this, argu);
	  program.add(";");

	  try{

		
		bw.flush();
		bw.close();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }
      return _ret;
   }



   /**
    * f0 -> FormalParameter()
    * f1 -> FormalParameterTail()
    */
   public String visit(FormalParameterList n, String argu) {
      String _ret=null;
      n.f0.accept(this, argu);
      String name=n.f1.accept(this, argu);
	  //System.out.println("EDWWWWWWWWWWWWW"+" name "+name+"  "+argu);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public String visit(FormalParameter n, String argu) {
      String _ret=null;

	  String filename=previousprogram.get(1)+".ll";
	  BufferedWriter bw=null;
	  try{
		  File f=new File(filename);
		  /*if(!file.exists()){
			file.createNewFile();
		  }*/
		  FileWriter fw=new FileWriter(f,true);
				
		  bw=new BufferedWriter(fw);
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

	  n.f0.accept(this, argu);
      String type=n.f1.accept(this, argu);

	  
	  try{
		


		bw.close();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }
	  //System.out.println("NAMEEEEEEEEEE "+name);
      return type;
   }

   /**
    * f0 -> ( FormalParameterTerm() )*
    */
   public String visit(FormalParameterTail n, String argu) {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public String visit(FormalParameterTerm n, String argu) {
      String _ret=null;
      n.f0.accept(this, argu);
	  program.add(",");
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public String visit(Type n, String argu) {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> BooleanArrayType()
    *       | IntegerArrayType()
    */
   public String visit(ArrayType n, String argu) {
	  String array=n.f0.accept(this, argu);
	  program.add(array);
      return array;
   }

   /**
    * f0 -> "boolean"
    * f1 -> "["
    * f2 -> "]"
    */
   public String visit(BooleanArrayType n, String argu) {
      return "boolean[]";
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public String visit(IntegerArrayType n, String argu) {
      return "int[]";
   }

   /**
    * f0 -> "boolean"
    */
   public String visit(BooleanType n, String argu) {
	  //System.out.println("return "+"true or false "+"Method:"+argu);
	  String bool=n.f0.accept(this, argu);
	  program.add(bool);
      return "boolean";
   }

   /**
    * f0 -> "int"
    */
   public String visit(IntegerType n, String argu) {
	  String integer=n.f0.accept(this, argu);
	  program.add(integer);
      return "int";
   }

   /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    */
   public String visit(Statement n, String argu) {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
   public String visit(Block n, String argu) {
      String _ret=null;
      n.f0.accept(this, argu);
	  program.add("{");
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
	  program.add("}");
      return _ret;
   }

   /**
    * f0 -> Identifier()////////////////////////////////////////////////////////////////////////////////
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
   public String visit(AssignmentStatement n, String argu) {
      String _ret=null;
	  String filename=previousprogram.get(1)+".ll";
	  BufferedWriter bw=null;
	  try{
		  File f=new File(filename);
		  /*if(!file.exists()){
			file.createNewFile();
		  }*/
		  FileWriter fw=new FileWriter(f,true);
				
		  bw=new BufferedWriter(fw);
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }



	  String type1,type2;
	  type1=n.f0.accept(this,argu);
	  String name1=program.get(program.size()-1);
	  try{
		/*bw.write("%_"+numregister+" = load ");
		numregister++;
		if(type1=="int"){
			bw.write("i32, i32* %"+program.get(program.size()-1)+"\n");
		}*/
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

      n.f1.accept(this, argu);
	  program.add("=");
      type2=n.f2.accept(this, argu);//o typos toy antikeimenoy einai type1
	  
	  try{
		bw.write("store ");
		if(isNumeric(program.get(program.size()-1))&&program.get(program.size()-2)=="="){
			if(program.get(program.size()-1)=="]"){
					bw.write("i32 %_"+(numregister-1)+", i32* %"+(numregister-1)+"\n");
			}
			else{
				bw.write("i32 "+program.get(program.size()-1)+", i32* %"+program.get(program.size()-3)+"\n");
			}
		}
		else{
			if(type1=="int"||type1=="int[]"){
				if(program.get(program.size()-1)=="]"){
					bw.write("i32 %_"+(numregister-1)+", i32* %"+(numregister-4)+"\n");
				}
				else{
					bw.write("i32 %_"+(numregister-1)+", i32* %"+name1+"\n");
				}
			}
		}
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

	  
      n.f3.accept(this, argu);
	  program.add(";");
	
      return _ret;
   }

   /**
    * f0 -> Identifier()
    * f1 -> "["
    * f2 -> Expression()
    * f3 -> "]"
    * f4 -> "="
    * f5 -> Expression()
    * f6 -> ";"
    */
   public String visit(ArrayAssignmentStatement n, String argu) {
      String _ret=null;
	  String filename=previousprogram.get(1)+".ll";
	  BufferedWriter bw=null;
	  try{
		  File f=new File(filename);
		  /*if(!file.exists()){
			file.createNewFile();
		  }*/
		  FileWriter fw=new FileWriter(f,true);
				
		  bw=new BufferedWriter(fw);
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }


	  String typeofarray;
      typeofarray=n.f0.accept(this, argu);
	  int registerf0=0;
	  try{
	  	bw.write("%_"+numregister+" = load i32, i32 *%_"+(numregister-1)+"\n");
		registerf0=numregister;
		numregister++;
		
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

	  if(typeofarray=="int[]"){
		typeofarray="int";
	  }
	  else if(typeofarray=="boolean[]"){
		typeofarray="boolean";
	  }
	  else{
		//System.out.println("This variable is not an array");
		throw new ParseError();
	  }
      n.f1.accept(this, argu);
	  program.add("[");
      String typeofnumberofelement=n.f2.accept(this, argu);
	  int registerf2=numregister-1;
	  String thesi=program.get(program.size()-1);
	  int registerparseerror=ifregister;
	  int registercontinue=ifregister+1;
	  int finalreg=ifregister+2;
	  ifregister+=3;
	  try{
		if(isNumeric(thesi)){
			bw.write("%_"+numregister+" = icmp ult i32 "+thesi+", %_"+(numregister-1)+"\n");
		}
		else{
			bw.write("%_"+numregister+" = icmp ult i32 %_"+registerf2+", %_"+(numregister-1)+"\n");
		}
		numregister++;
		bw.write("br i1 %_"+(numregister-1)+", label %oob"+registercontinue+", label %oob"+registerparseerror+"\n");
		bw.write("oob"+registercontinue+":\n");
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

      n.f3.accept(this, argu);
	  program.add("]");
      n.f4.accept(this, argu);
	  program.add("=");
      String typeofexpr=n.f5.accept(this, argu);
	  try{
			bw.write("%_"+numregister+" = add i32 "+thesi+", "+program.get(program.size()-1));
			numregister++;
			bw.write("%_"+numregister+" = getelementptr i32, i32* %_"+registerf0+", i32 %_"+(numregister-1)+"\n");
			numregister++;
			bw.write("store i32 "+program.get(program.size()-1)+", i32* %_"+(numregister-1)+"\n");
			bw.write("br label %oob"+finalreg+"\n\n");


			bw.write("oob"+registerparseerror+":\n");
			bw.write("call void @throw_oob()\n");
			bw.write("br label %oob"+finalreg+"\n\n");

			bw.write("oob"+finalreg+":\n\n");
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }
      n.f6.accept(this, argu);
	  program.add(";");
      return typeofarray;
   }

   /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    * f5 -> "else"
    * f6 -> Statement()
    */
   public String visit(IfStatement n, String argu) {
      String _ret=null;

	  String filename=previousprogram.get(1)+".ll";
	  BufferedWriter bw=null;
	  try{
		  File f=new File(filename);
		  /*if(!file.exists()){
			file.createNewFile();
		  }*/
		  FileWriter fw=new FileWriter(f,true);
				
		  bw=new BufferedWriter(fw);
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

      n.f0.accept(this, argu);
	  program.add("if");
      n.f1.accept(this, argu);
	  program.add("(");
      String register=n.f2.accept(this, argu);
	  
	  try{
		bw.write("br i1 "+register+", label %if"+ifregister+", label %if"+(ifregister+1)+"\n");
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }
	  int registerforif=ifregister;
	  int registerforelse=ifregister+1;
	  int registerafter=ifregister+2;
	  ifregister+=3;

      n.f3.accept(this, argu);
	  program.add(")");

	  try{
		bw.write("if"+registerforif+":\n");
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }
	

      n.f4.accept(this, argu);

	  try{
		bw.write("br label %if"+registerafter+"\n");
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }
	  

      n.f5.accept(this, argu);
	  program.add("else");

	  try{
		bw.write("if"+registerforelse+":\n");
		ifregister++;
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

      n.f6.accept(this, argu);
	  try{
		bw.write("br label %if"+registerafter+"\n");
		bw.write("if"+registerafter+":\n");
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }
      return _ret;
   }

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public String visit(WhileStatement n, String argu) {
      String _ret=null;
	  String filename=previousprogram.get(1)+".ll";
	  BufferedWriter bw=null;
	  try{
		  File f=new File(filename);
		  /*if(!file.exists()){
			file.createNewFile();
		  }*/
		  FileWriter fw=new FileWriter(f,true);
				
		  bw=new BufferedWriter(fw);
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }
	  
      n.f0.accept(this, argu);
	  program.add("while");
      n.f1.accept(this, argu);
	  
	  program.add("(");
      String expr=n.f2.accept(this, argu);
	  
      n.f3.accept(this, argu);
	  int bodywhileregister=ifregister;
	  int bodyafterregister=ifregister+1;
	  ifregister+=2;
	  try{
		bw.write("br i1 %_"+(numregister-1)+", label %loop"+bodywhileregister+", label %loop"+bodyafterregister+"\n");
		bw.write("loop"+bodywhileregister+":\n");
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

	  program.add(")");
      n.f4.accept(this, argu);
	  
	  try{
		bw.write("br label %loop"+bodyafterregister+"\n");
		bw.write("loop"+bodyafterregister+":\n");
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

      return _ret;
   }

   /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
   public String visit(PrintStatement n, String argu) {
      String _ret=null;

	  String filename=previousprogram.get(1)+".ll";
	  BufferedWriter bw=null;
	  try{
		  File f=new File(filename);
		  /*if(!file.exists()){
			file.createNewFile();
		  }*/
		  FileWriter fw=new FileWriter(f,true);
				
		  bw=new BufferedWriter(fw);
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

      n.f0.accept(this, argu);
	  program.add("System.out.println");
      n.f1.accept(this, argu);
	  program.add("(");
      String type=n.f2.accept(this, argu);
	  int registerf2=numregister-1;
	  int index=0;
	  

	  try{
		bw.write("call void ");
		if(type=="int"){
			bw.write("(i32) @print_int(i32 %_"+registerf2+")\n");
		}
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

      n.f3.accept(this, argu);
	  program.add(")");
      n.f4.accept(this, argu);
	  program.add(";");
      return _ret;
   }

   /**
    * f0 -> AndExpression()
    *       | CompareExpression()
    *       | PlusExpression()
    *       | MinusExpression()
    *       | TimesExpression()
    *       | ArrayLookup()
    *       | ArrayLength()
    *       | MessageSend()
    *       | Clause()
    */
   public String visit(Expression n, String argu) {
	  String type=n.f0.accept(this, argu);
      return type;
   }

   /**
    * f0 -> Clause()
    * f1 -> "&&"
    * f2 -> Clause()
    */
   public String visit(AndExpression n, String argu) {
      String _ret=null;
      String type1=n.f0.accept(this, argu);
      n.f1.accept(this, argu);
	  program.add("&&");
      String type2=n.f2.accept(this, argu);
	  if(type1!="boolean"||type2!="boolean"){
		//System.out.println("The && expression arent of boolean types");
		throw new ParseError();
	  }
      return "boolean";
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
   public String visit(CompareExpression n, String argu) {
      String _ret=null;

	  String filename=previousprogram.get(1)+".ll";
	  BufferedWriter bw=null;
	  try{
		  File f=new File(filename);
		  /*if(!file.exists()){
			file.createNewFile();
		  }*/
		  FileWriter fw=new FileWriter(f,true);
				
		  bw=new BufferedWriter(fw);
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

      String type1=n.f0.accept(this, argu);
	  int registerf0=numregister-1;

      n.f1.accept(this, argu);
	  program.add("<");
      String type2=n.f2.accept(this, argu);
	  int registerf2=numregister-1;
	  if(!isNumeric(program.get(program.size()-1))){
		  try{
			/*bw.write("%_"+numregister+" = load ");
			numregister++;
			bw.write("i32, i32* %"+program.get(program.size()-1)+"\n");*/
			bw.write("%_"+numregister+" = icmp slt i32 %_"+registerf0+", %_"+registerf2+"\n");
			numregister++;
			bw.flush();
		 }
		 catch (IOException e) {
		      e.printStackTrace();
		 }
	  }
	  else{
		try{
			bw.write("%_"+numregister+" = icmp slt i32 %_"+registerf0+", "+program.get(program.size()-1)+"\n");
			numregister++;
			bw.flush();
		 }
		 catch (IOException e) {
		      e.printStackTrace();
		 }
	  }
	
	  try{
		bw.close();
	  }
	  catch (IOException e) {
	      e.printStackTrace();
	  }
      return "%_"+(numregister-1);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public String visit(PlusExpression n, String argu) {
      String _ret=null;
	  String filename=previousprogram.get(1)+".ll";
	  BufferedWriter bw=null;
	  try{
		  File f=new File(filename);
		  /*if(!file.exists()){
			file.createNewFile();
		  }*/
		  FileWriter fw=new FileWriter(f,true);
				
		  bw=new BufferedWriter(fw);
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }
	  
	  String type1,type2;
      type1=n.f0.accept(this, argu);
	  String var1=program.get(program.size()-1);
	  int numeric1=0;
	  if(isNumeric(var1)){
		numeric1=1;
	  }
	  int register1=numregister-1;
      n.f1.accept(this, argu);
	  program.add("+");
      type2=n.f2.accept(this, argu);
	  String var2=program.get(program.size()-1);
	  int numeric2=0;
	  if(isNumeric(var2)){
		numeric2=1;
	  }
	  int register2=numregister-1;

	  try{
		if(numeric1==1){//an exoume integerliteral sto prwto skelos toy sub
			if(numeric2==1){//an exoume literal kai sto 2o
				bw.write("%_"+numregister+" = add i32 "+var1+", "+var2+"\n");
				numregister++;
			}
			else{//exoume metablhth sto 2o
				bw.write("%_"+numregister+" = add i32 "+var1+", %_"+register2+"\n");
				numregister++;
			}
		}
		else{//an exoume metablhth sto 1o
			if(numeric2==1){//literal sto 2o
				bw.write("%_"+numregister+" = add i32 %_"+register1+", "+var2+"\n");
				numregister++;
			}
			else{//an exoume metablhth kai sto 2o
				bw.write("%_"+numregister+" = add i32 %_"+register1+", %_"+register2+"\n");
				numregister++;
			}
	
		}
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

      return type1;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public String visit(MinusExpression n, String argu) {
      String _ret=null;
	  
	  String filename=previousprogram.get(1)+".ll";
	  BufferedWriter bw=null;
	  try{
		  File f=new File(filename);
		  /*if(!file.exists()){
			file.createNewFile();
		  }*/
		  FileWriter fw=new FileWriter(f,true);
				
		  bw=new BufferedWriter(fw);
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

	  String type1,type2;
      type1=n.f0.accept(this, argu);
	  String var1=program.get(program.size()-1);
	  int numeric1=0;
	  if(isNumeric(var1)){
		numeric1=1;
	  }
	  int register1=numregister-1;
      n.f1.accept(this, argu);
	  program.add("-");
      type2=n.f2.accept(this, argu);
	  String var2=program.get(program.size()-1);
	  int numeric2=0;
	  if(isNumeric(var2)){
		numeric2=1;
	  }
	  int register2=numregister-1;

	  try{
		if(numeric1==1){//an exoume integerliteral sto prwto skelos toy sub
			if(numeric2==1){//an exoume literal kai sto 2o
				bw.write("%_"+numregister+" = sub i32 "+var1+", "+var2+"\n");
				numregister++;
			}
			else{//exoume metablhth sto 2o
				bw.write("%_"+numregister+" = sub i32 "+var1+", %_"+register2+"\n");
				numregister++;
			}
		}
		else{//an exoume metablhth sto 1o
			if(numeric2==1){//literal sto 2o
				bw.write("%_"+numregister+" = sub i32 %_"+register1+", "+var2+"\n");
				numregister++;
			}
			else{//an exoume metablhth kai sto 2o
				bw.write("%_"+numregister+" = sub i32 %_"+register1+", %_"+register2+"\n");
				numregister++;
			}
	
		}
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

      return type1;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public String visit(TimesExpression n, String argu) {
      String _ret=null;

	  String filename=previousprogram.get(1)+".ll";
	  BufferedWriter bw=null;
	  try{
		  File f=new File(filename);
		  /*if(!file.exists()){
			file.createNewFile();
		  }*/
		  FileWriter fw=new FileWriter(f,true);
				
		  bw=new BufferedWriter(fw);
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

	  String type1,type2;
      type1=n.f0.accept(this, argu);
	  
	  try{
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }
	  int registerf0=numregister-1;

      n.f1.accept(this, argu);
	  program.add("*");
      type2=n.f2.accept(this, argu);
	  int registerf2=numregister-1;
	 

	  try{
	    bw.write("%_"+numregister+" = mul i32 %_"+registerf0+", %_"+registerf2+"\n");
		numregister++;
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }
	  
      return type1;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public String visit(ArrayLookup n, String argu) {
      String _ret=null;
	  String filename=previousprogram.get(1)+".ll";
	  BufferedWriter bw=null;
	  try{
		  File f=new File(filename);
		  /*if(!file.exists()){
			file.createNewFile();
		  }*/
		  FileWriter fw=new FileWriter(f,true);
				
		  bw=new BufferedWriter(fw);
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }


      String typeofarray=n.f0.accept(this, argu);
	  String nameofarray=program.get(program.size()-1);
	  //System.out.println("PROGR:"+nameofarray);
	  int index=program.size()-1;
	  while(program.get(index)!="class"){
		index--;
	  }
	  int flag=0;
	  int offsets=8;
	  while(program.get(index)!="public"){
		if(program.get(index)==nameofarray){
			flag=1;
			break;
		}
		else if(program.get(index-1)=="int"){
			offsets+=4;
		}
		else if(program.get(index-1)=="boolean"){
			offsets+=1;
		}
		else if(program.get(index-1)=="int[]"||program.get(index-1)=="boolean[]"){
			offsets+=8;
		}
		index++;
	  }
	  

	  if(typeofarray=="int[]"){
		typeofarray="int";
	  }
	  else if(typeofarray=="boolean[]"){
		typeofarray="boolean";
	  }
      n.f1.accept(this, argu);
	  program.add("[");
      String num=n.f2.accept(this, argu);
	  
	  if(flag==1){//ean exoume array ths klashs
		try{
			
			bw.write("%_"+numregister+" = add i32 %_"+(numregister-1)+", 1\n");
			numregister++;
			bw.write("%_"+numregister+" = getelementptr i32, i32* %_"+(numregister-3)+", i32 %_"+(numregister-1)+"\n");
			numregister++;
			bw.write("%_"+numregister+" = load i32, i32* %_"+(numregister-1)+"\n");	
			numregister++;

			bw.flush();
		}
		catch (IOException e) {
          e.printStackTrace();
      	}
	  }

      n.f3.accept(this, argu);
	  program.add("]");
      return typeofarray;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public String visit(ArrayLength n, String argu) {
      String _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
	  program.add(".");
      n.f2.accept(this, argu);
	  program.add("length");
      return "int";
   }

   /**
    * f0 -> PrimaryExpression()/////////////////////////////////////////////////////////////
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
   public String visit(MessageSend n, String argu) throws RuntimeException{
      String _ret=null;
	  String filename=previousprogram.get(1)+".ll";
	  BufferedWriter bw=null;
	  try{
		  File f=new File(filename);
		  /*if(!file.exists()){
			file.createNewFile();
		  }*/
		  FileWriter fw=new FileWriter(f,true);
				
		  bw=new BufferedWriter(fw);
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }


	  String objecttype,funtype;	  
      objecttype=n.f0.accept(this, argu);

	  String thisornot=objecttype;
	  
	  if(objecttype=="this"){
			int index=program.size()-1;
			while(index!=0){
				if(program.get(index)=="class"){
					break;
				}
				index--;
			}
			objecttype=program.get(index+1);
			//System.out.println("ELAAAAAAAAAAAAAAAA"+klash);
			n.f1.accept(this, argu);
			program.add(".");
		  	funtype=n.f2.accept(this, objecttype);//pernaw san orisma to typo toy antikeimenoy
	  }
	  else{
			n.f1.accept(this, argu);
	  		program.add(".");
	  		funtype=n.f2.accept(this, objecttype);//pernaw san orisma to typo toy antikeimenoy

	  }

	  String funname=program.get(program.size()-1);//to onoma ths synarthshs
	  int registeroffun=0;

	  int index=0;
	  while(index!=previousprogram.size()-2){//gia na broume ton arithmo ths synarthshs
		if(previousprogram.get(index)=="public"&&previousprogram.get(index+2)==funname){
			break;
		}
		index++;
	  }
	  index--;
	  int numoffun=0;
	  while(previousprogram.get(index)!="class"){
		if(previousprogram.get(index)=="public"){
			numoffun++;
		}
		index--;
	  }
	
	  try{
		if(funtype=="int"){
			bw.write("%_"+numregister+" = bitcast i8* %_"+(numregister-1)+" to i32 (i8*");
			registeroffun=numregister;
			numregister++;
			index=0;
			while(index!=previousprogram.size()-2){
				if(previousprogram.get(index)=="public"&&previousprogram.get(index+2)==funname){
					index+=4;
					while(previousprogram.get(index)!=")"){
						if(previousprogram.get(index)=="int"){
							bw.write(",i32");
						}
						else if(previousprogram.get(index)=="boolean"){
							bw.write(",i1");
						}
						index++;
					}
					break;
				}
				index++;
			}

			bw.write(")*\n");
			
		}
		
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

	  
	  //System.out.println("OBJECTTYPE "+objecttype+" functiontype "+funtype+" functionNAME "+funname);
	  ////////////psaksimo twn arguements ths synarthshs ayths	  
      n.f3.accept(this, argu);
	  program.add("(");
      String type=n.f4.accept(this, argu);
	  int registeroff4=numregister-1;
	  try{
		bw.write("%_"+numregister+" = call ");
		numregister++;
		if(funtype=="int"){
			bw.write("i32 %_"+registeroffun+"(i8* ");
			if(thisornot=="this"){
				bw.write("%this");
				index=0;
				
				while(index!=previousprogram.size()-2){
					if(previousprogram.get(index)=="public"&&previousprogram.get(index+2)==funname){
						index+=4;
						int index2=index;
						int currentreg=numregister-2;
						while(previousprogram.get(index2)!=")"){
							if(previousprogram.get(index2)==","){
								currentreg--;
							}
							index2++;
						}
						
						
						while(previousprogram.get(index)!=")"){///////////////////////EDWWWWWWWWWWWW THELEI KATI GIA TA LITERALS
							if(previousprogram.get(index)=="int"){
								bw.write(", i32 %_"+currentreg);
								currentreg++;
							}
							else if(previousprogram.get(index)=="boolean"){
								bw.write(", i1 %_"+currentreg);
								currentreg++;
							}
							index++;
							bw.flush();
						}
						bw.write(")\n");
						
						break;
					}
					index++;
				}
			}
			else if(argu=="main"){
				
				bw.write("%_0");
				index=0;
				int numofargs=0;
				while(index!=previousprogram.size()-2){
					if(previousprogram.get(index)=="public"&&previousprogram.get(index+2)==funname){
						index+=4;
						if(previousprogram.get(index)!=")"){
							numofargs=1;
							while(previousprogram.get(index)!=")"){
								if(previousprogram.get(index)==","){
									numofargs++;	
								}

								index++;
							}
						}
						break;
					}

					index++;
				}

				index=0;
				while(index!=previousprogram.size()-2){
					if(previousprogram.get(index)=="public"&&previousprogram.get(index+2)==funname){
						index+=4;
						if(previousprogram.get(index)==")"){
							bw.write(")\n");
							break;
						}
						while(previousprogram.get(index)!=")"){
							if(previousprogram.get(index)==","){
								if(previousprogram.get(index-2)=="int"){
									bw.write(", i32 "+(program.get(program.size()-2*numofargs+1)));
									
								}

								numofargs--;
							}
							index++;
						}

						if(previousprogram.get(index-2)=="int"){
							bw.write(", i32 "+(program.get(program.size()-1))+")\n");
						}
						else if(previousprogram.get(index-2)=="boolean"){
							bw.write(", i32 "+(program.get(program.size()-1))+")\n");
						}
						bw.flush();
						break;
					}
					index++;
				}
				bw.flush();
			}
			
			bw.flush();
		}
		
		
			
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

      n.f5.accept(this, argu);
	  program.add(")");
	  //System.out.println("ARGS "+args+" PARS "+pars);



	  pars.clear();//adeiazw ksana ton vector
	  args.clear();//adeiasma
      return funtype;
   }

   /**
    * f0 -> Expression()
    * f1 -> ExpressionTail()
    */
   public String visit(ExpressionList n, String argu) {
	  
      String _ret=null;
      String type1=n.f0.accept(this, argu);
	  if(type1=="this"){//ama brhkame this
		 int index=program.size()-1;
		 while(index!=0){
			if(program.get(index)=="class"){
				pars.add(program.get(index+1));
				break;
			}
			index--;
		 }
		 
	  }
	  else{
	  	pars.add(type1);
	  }
      String type2=n.f1.accept(this, argu);
      return type1;
   }

   /**
    * f0 -> ( ExpressionTerm() )*
    */
   public String visit(ExpressionTail n, String argu) {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public String visit(ExpressionTerm n, String argu) {
      String _ret=null;
      n.f0.accept(this, argu);
	  program.add(",");
      String type=n.f1.accept(this, argu);
	  //System.out.println("MMMMMMMMMMMMMMMMMMMMMMMMMAAAAAAALLLLLLLLLLLLLLLLLLLLLLLLLLAAAKAAAAAAAAAAAAAAAAAAAAAAA"+type);
	  pars.add(type);
      return type;
   }

   /**
    * f0 -> NotExpression()
    *       | PrimaryExpression()
    */
   public String visit(Clause n, String argu) {
	  String type=n.f0.accept(this, argu);
      return type;
   }

   /**
    * f0 -> IntegerLiteral()
    *       | TrueLiteral()
    *       | FalseLiteral()
    *       | Identifier()
    *       | ThisExpression()
    *       | ArrayAllocationExpression()
    *       | AllocationExpression()
    *       | BracketExpression()
    */
   public String visit(PrimaryExpression n, String argu) {
	  String type=n.f0.accept(this, argu);
      return type;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public String visit(IntegerLiteral n, String argu) {
	  String number; 
      number=n.f0.accept(this, argu);
	  int index=program.size()-1;
	  program.add(number);
	  return "int";
   }

   /**
    * f0 -> "true"
    */
   public String visit(TrueLiteral n, String argu) {
	  n.f0.accept(this, argu);
	  program.add("true");
      return "boolean";
   }

   /**
    * f0 -> "false"
    */
   public String visit(FalseLiteral n, String argu) throws ParseError{
	  n.f0.accept(this, argu);
	  program.add("false");
	  return "boolean";
      
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public String visit(Identifier n, String argu) {
	  

	  String filename=previousprogram.get(1)+".ll";
	  BufferedWriter bw=null;
	  try{
		  File f=new File(filename);
		  /*if(!file.exists()){
			file.createNewFile();
		  }*/
		  FileWriter fw=new FileWriter(f,true);
				
		  bw=new BufferedWriter(fw);
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }


	  String name;
	  String type=" ";
	  name=n.f0.accept(this, argu);
	  if(program.size()>4){
		  if(program.get(program.size()-2)=="public"){//exoume orismo synarthshs
			program.add(name);
			return program.get(program.size()-2);//epistrefoyme ton typo epistrofhs
		  }
	  }
      
	  
	  if(argu!=null){//shmainei oti den eimaste se orismo klashs
		if(program.size()-1>3){
			String typeornot=program.get(program.size()-1);
			int flagdeclareornot=0;
			int index=0;
			while(index!=previousprogram.size()-1){

				if(previousprogram.get(index)=="class"&&previousprogram.get(index+1)==typeornot){
					flagdeclareornot=1;
					break;
				}
				index++;
			}	
			if(typeornot=="int"||typeornot=="boolean"||typeornot=="int[]"||typeornot=="boolean[]"){
				flagdeclareornot=1;
			}
			if(flagdeclareornot==1){//exoume orismo metablhths
				index=program.size()-1;
				while(program.get(index)!="class"&&program.get(index)!="public"){
					index--;

				}
				if(program.get(index)=="class"){//eimaste se metablhtes klashs
					index+=2;
					while(program.get(index)!="public"&&index!=program.size()-1){
						if(program.get(index)==name){
							//System.out.println("Variable "+name+" is alreadyyyyy declared");
							throw new ParseError();
						}
						index++;
					}
				}
				else{//an eimaste se metablhtes synarthshs
					index++;
					while(program.get(index)!="public"&&index!=program.size()-1){
						if(program.get(index)==name){
							//System.out.println("Variable "+name+" is already declared");
							throw new ParseError();
						}
						index++;
					}
					while(program.get(index)!="class"){
						index--;
					}
					while(program.get(index)!="public"&&index!=program.size()-1){
						if(program.get(index)==name){
							//System.out.println("Variable "+name+" is alreadyy declared");
							throw new ParseError();
						}
						index++;
					}
				}
				type=typeornot;

			}
			else{//alliws den exoume orismo metablhths
				try{
					if(checkifitsavariableofclass(name)){//an einai metablhth ths klashs
						index=program.size()-1;
						int numofvar=8;
						while(program.get(index)!="class"){
							index--;
						}
						while(program.get(index)!="public"){
							if(program.get(index)==name){
								break;
							}
							if(program.get(index)==";"){
								if(program.get(index-2)=="int[]"||program.get(index-2)=="boolean[]"){
									numofvar+=8;
								}
								else if(program.get(index-2)=="int"){
									numofvar+=4;
								}
								else if(program.get(index-2)=="boolean"){
									numofvar+=1;
								}
							}
							index++;
						}
						bw.write("%_"+numregister+" = getelementptr i8, i8* %this, ");
						numregister++;
						bw.write("i32 "+numofvar+"\n");
						bw.write("%_"+numregister+" = bitcast i8* %_"+(numregister-1)+" to i32**\n");
						numregister++;
						bw.write("%_"+numregister+" = load i32*, i32** %_"+(numregister-1)+"\n");
						numregister++;
						
						bw.flush();
					}
					bw.flush();
				}
				catch (IOException e) {
				    e.printStackTrace();
				}


				index=program.size()-1;
				if(previousprogram.get(index)=="new"&&previousprogram.get(index+2)=="("){
					type=name;
				}
				else if(previousprogram.get(index+2)=="("){//tote exoume synarthsh
					//System.out.println("PRRRRRRRRRR "+previousprogram.get(index));
					type=superclassesforfunctions(argu,name);
					if(type==";"){
						//System.out.println("Function "+name+" does not exist in class "+argu);
						throw new ParseError();
					}
					//System.out.println("function "+name+" kai typo epistrofhs "+type);
				}
				else{//alliws exoume metablhth
					index=program.size()-1;
					while(program.get(index)!="class"){//ftanoume sthn arxh ths klashs
						index--;
					}
					String klashpoyeimaste=program.get(index+1);
					type=superclassesforvariables(klashpoyeimaste,name);
					if(type==";"){//tote einai sth synarthsh
						index=program.size()-1;
						while(program.get(index)!="public"){//pame sthn arxh ths synarthshs
							index--;
						}
						while(index!=program.size()){
							if(program.get(index)==name){
								type=program.get(index-1);
								break;
							}
							index++;
						}
					}
					//System.out.println("H metablhth einai "+name+" kai o typos "+type+" kai h klash poy eimaste "+klashpoyeimaste);
				}
			}

		  }
		}
	 program.add(name);

	 try{
		int index=0;
		int numoffun=0;
		while(index!=previousprogram.size()-2){
			if(previousprogram.get(index)=="public"&&previousprogram.get(index+2)==name){
				index--;
				while(previousprogram.get(index)!="class"){
					if(previousprogram.get(index)=="public"){
						numoffun++;
					}
					index--;
				}
				break;
			}
			index++;
		}

		
		
		index=0;
		int funornot=0;
		while(index!=previousprogram.size()-2){
			if(previousprogram.get(index)=="public"&&previousprogram.get(index+2)==name){
				bw.write("%_"+numregister+" = load ");
				numregister++;
				bw.write("i8**,i8*** %_"+(numregister-2)+"\n");
				funornot=1;
				bw.write("%_"+numregister+" = getelementptr i8*, i8** %_"+(numregister-1)+", i32 "+numoffun+"\n");
	    		numregister++;
				bw.write("%_"+numregister+" = load i8*, i8** %_"+(numregister-1)+"\n");
				numregister++;
				bw.flush();

				break;
			}
			index++;
		}


		int classornot=0;
		index=program.size()-1;
		while(index!=0){
			if(program.get(index)=="public"){
				classornot=0;
				break;
			}
			else if(program.get(index)=="class"){//tote milame gia metablhtes klashs
				classornot=1;
				break;
			}
			index--;
		}
		if(classornot==0){
			if(funornot==0&&type=="int"){
				if(program.get(program.size()-2)=="int"){//an exoume orismo metablhths
					bw.write("%"+program.get(program.size()-1)+" = alloca i32\n");
					index=program.size()-1;
					while(program.get(index)!="("&&program.get(index)!=")"){
						index--;
					}
					if(program.get(index-3)=="public"){//tote milame gia parametrous synarthshs opote theloume store
						bw.write("store i32 %."+program.get(program.size()-1)+", i32* %"+program.get(program.size()-1)+"\n");
					}
					bw.flush();
				}
				bw.write("%_"+numregister+" = load ");
				numregister++;
				bw.write("i32, i32* %"+name+"\n");
			}
			else if(funornot==0&&type=="boolean"){
				if(program.get(program.size()-2)=="boolean"){
					bw.write("%"+program.get(program.size()-1)+" = alloca i8\n");

					index=program.size()-1;
					while(program.get(index)!="("){
						index--;
					}
					if(program.get(index-3)=="public"){//tote milame gia parametrous synarthshs opote theloume store
						bw.write("store i8 %."+program.get(program.size()-1)+", i8* %"+program.get(program.size()-1)+"\n");
					}
					
					bw.flush();
				}
				bw.write("%_"+numregister+" = load ");
				numregister++;
				bw.write("i32, i32* %"+program.get(program.size()-1)+"\n");
			}
		}
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

	 return type;
   }

   /**
    * f0 -> "this"
    */
   public String visit(ThisExpression n, String argu) {

	  String filename=previousprogram.get(1)+".ll";
	  BufferedWriter bw=null;
	  try{
		  File f=new File(filename);
		  /*if(!file.exists()){
			file.createNewFile();
		  }*/
		  FileWriter fw=new FileWriter(f,true);
				
		  bw=new BufferedWriter(fw);
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }


	  program.add("this");

	  try{
		bw.write("%_"+numregister+" = bitcast i8* %this to i8***\n");
		numregister++;
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

	  int index=program.size()-1;
	  if(previousprogram.get(index+1)==";"){//shmainei oti kaleitai sketo this
		int index2=index;
		while(program.get(index2)!="class"){
			index2--;
		}
		String classname=program.get(index2+1);
		n.f0.accept(this, argu);
		return classname;
	  }
	  else{//alliws kaleitai synarthsh me to this
		  while(program.get(index)!="class"){
			index--;
		  }
		  String klash=program.get(index+1);
		  String type=n.f0.accept(this, klash);
		  return "this";
	  }
   }

   /**
    * f0 -> BooleanArrayAllocationExpression()
    *       | IntegerArrayAllocationExpression()
    */
   public String visit(ArrayAllocationExpression n, String argu) {
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> "new"
    * f1 -> "boolean"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public String visit(BooleanArrayAllocationExpression n, String argu) {
      String _ret=null;
      n.f0.accept(this, argu);
	  program.add("new");
      n.f1.accept(this, argu);
	  program.add("boolean");
      n.f2.accept(this, argu);
	  program.add("[");
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
	  program.add("]");
      return "boolean[]";
   }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public String visit(IntegerArrayAllocationExpression n, String argu) {
      String _ret=null;

	  String filename=previousprogram.get(1)+".ll";
	  BufferedWriter bw=null;
	  try{
		  File f=new File(filename);
		  /*if(!file.exists()){
			file.createNewFile();
		  }*/
		  FileWriter fw=new FileWriter(f,true);
				
		  bw=new BufferedWriter(fw);
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

      n.f0.accept(this, argu);
	  program.add("new");
      n.f1.accept(this, argu);
	  program.add("int");
      n.f2.accept(this, argu);
	  program.add("[");
      n.f3.accept(this, argu);
	  int registerf3=numregister-1;
      n.f4.accept(this, argu);

	  try{
		bw.write("%_"+numregister+" = icmp ult i32 %_"+(numregister-1)+", 0\n");
		numregister++;
		bw.write("i1 %_"+(numregister-1)+", label %arr_alloc"+ifregister+",label %arr_alloc"+(ifregister+1)+"\n");
		bw.write("arr_alloc"+ifregister+":\n");
		bw.write("call void @throw_oob()\n");
		bw.write("br label %arr_alloc"+(ifregister+1)+"\n");
		ifregister++;
		bw.write("arr_alloc"+ifregister+":\n");
		ifregister++;
		bw.write("%_"+numregister+" = add i32 %_"+registerf3+",1\n");
		numregister++;
		bw.write("%_"+numregister+" = call i8* @calloc(i32 4, i32 %_"+(numregister-1)+")\n");
		numregister++;
		bw.write("%_"+numregister+" = bitcast i8* %_"+(numregister-1)+" to i32*\n");
		numregister++;
		bw.write("store i32 %_"+registerf3+", i32* %_"+(numregister-1)+"\n");
		bw.flush();
	  }
	  catch (IOException e) {
          e.printStackTrace();
      }

	  program.add("]");
	  
      return "int[]";
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public String visit(AllocationExpression n, String argu) {
      String _ret=null;
      n.f0.accept(this, argu);
	  program.add("new");
      String type=n.f1.accept(this, argu);
      n.f2.accept(this, argu);
	  program.add("(");
      n.f3.accept(this, argu);
	  program.add(")");
      return type;
   }

   /**
    * f0 -> "!"
    * f1 -> Clause()
    */
   public String visit(NotExpression n, String argu) {
      String _ret=null;
      n.f0.accept(this, argu);
	  program.add("!");
      String type=n.f1.accept(this, argu);
      return type;
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public String visit(BracketExpression n, String argu) {
      String _ret=null;
      n.f0.accept(this, argu);
	  program.add("(");
      String type=n.f1.accept(this, argu);
	  //String funname=program.get(program.size()-1);//apothikeyoume to onoma ths synarthshs poy molis prosthesame sto pinaka
	  //System.out.println("EDWWWWWWWWWWWWW"+argu);
      n.f2.accept(this, argu);
	  program.add(")");
      return type;
   }

}

