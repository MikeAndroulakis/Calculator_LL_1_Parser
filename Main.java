import syntaxtree.*;
import visitor.*;
import java.io.*;
import java.io.IOException;
import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;



class Main {
    public static void main (String [] args){
	FileInputStream fis = null;
	for(int i=0;i<args.length;i++){
		String file=";";
		try{
			fis = new FileInputStream(args[i]);
			MiniJavaParser parser = new MiniJavaParser(fis);
			Goal root = parser.Goal();
			FirstVisitor first=new FirstVisitor();
			root.accept(first, null);
			ArrayList<String> program;
			program=first.ReturnOfArray();

			String filename=program.get(1);			
			
			filename=filename+".ll";

			PrintWriter writer=new PrintWriter(filename);
			writer.print("");
			writer.close();

			File f=new File(filename);
			/*if(!file.exists()){
				file.createNewFile();
			}*/
			FileWriter fw=new FileWriter(f,true);
			
			BufferedWriter bw=new BufferedWriter(fw);


			FileInputStream instream = null;
			FileOutputStream outstream = null;
			File infile =new File("copyfunctions.txt");
			File outfile =new File(filename);
			instream = new FileInputStream(infile);
			outstream = new FileOutputStream(outfile);

			byte[] buffer = new byte[1024];
    	    int length;
			
			while ((length = instream.read(buffer)) > 0){
    	    	outstream.write(buffer, 0, length);
    	    }


			bw.write("\n\n@."+filename+"_vtable = global [0 x i8*] []\n");
			bw.flush();
			int index2=4;
			while(index2!=program.size()-2){
				if(program.get(index2)=="class"){
					String classname=program.get(index2+1);
					bw.write("@."+classname+"_vtable");
					bw.flush();
					int numoffuns=0;
					int index3=index2;
					index3++;
					while(program.get(index3)!="class"&&index3!=program.size()-2){
						if(program.get(index3)=="public"){
							numoffuns++;
						}
						index3++;
					}
					bw.write(" = global ["+numoffuns+" x i8*]");
					bw.flush();
					if (numoffuns>=1){
						bw.write("[");
						bw.flush();
						index3=index2+1;
						int counteroffuns=0;
						while(program.get(index3)!="class"&&index3!=program.size()-2){
							if(program.get(index3)=="public"){
								String funname=program.get(index3+2);
								bw.write("i8* bitcast (");
								if(program.get(index3+1)=="int"){//an exoume epistrofh int
									bw.write("i32 (i8*");
								}
								else{//alliws exoume boolean
									bw.write("i1 (i8*");
								}
								
								int index4=index3+4;
								while(program.get(index4)!=")"){
									if(program.get(index4-1)==","||program.get(index4-1)=="("){
										bw.write(",");
									}
									if(program.get(index4)=="int"){
										bw.write("i32");
									}
									else if(program.get(index4)=="boolean"){
										bw.write("i1");
									}
									index4++;
								}
								bw.write(")* @"+classname+"."+funname+" to i8*)");

								bw.flush();
								counteroffuns++;
								if(counteroffuns!=numoffuns){
									bw.write(",");
									bw.flush();
								}
							}
							bw.flush();
							index3++;
						}
						bw.write("]\n\n");
						bw.flush();
					}

				}
				index2++;
			}

			bw.close();


			SecondVisitor second=new SecondVisitor();
			second.getArray(program);
			root.accept(second,null);


			file=filename;


			int indexforvars=0;
			int indexforpointers=0;
			ArrayList<String> offsets=new ArrayList<String>();
			int index=0;
			int size=program.size();
			//System.out.println("Size="+size);
			System.out.println("\nFile "+args[i]+" Parsed Successfully");
			file=args[i];
			System.out.println("The offsets of File "+file+" are:\n");
			if(program.get(5)=="void"){//eimaste sth main
				index+=4;
				while(index!=size){
					if(program.get(index)=="class"){
						break;
					}
					index++;
				}
			}
			if(index!=size){
				//System.out.println(program.get(index+1)+"ewwwwww");
				String klash=program.get(index+1);
				int flag=0;
				int flag2=0;
				while(index!=size){
					if(program.get(index)=="class"){
						if(flag==1){
							offsets.add(klash);
							offsets.add(Integer.toString(indexforvars));
							offsets.add(Integer.toString(indexforpointers));
							flag2=1;
						}
						else{
							flag=1;
						}
						indexforvars=0;
						indexforpointers=0;
						//System.out.println("EDW");
						klash=program.get(index+1);
						String superclass=";";
						if(flag2==1){
							if(program.get(index+2)=="extends"){
								
								if(offsets.contains(program.get(index+3))){
									superclass=program.get(index+3);
									//System.out.println("SUPER="+superclass+"  "+offsets);
									int indexofextend=offsets.indexOf(program.get(index+3));
									
									indexforvars=Integer.parseInt(offsets.get(indexofextend+1));
									indexforpointers=Integer.parseInt(offsets.get(indexofextend+2));
								}
							}
						}
						//System.out.println("klash "+klash);
						index2=index;
						index2++;
						while(index2!=size){
							if(program.get(index2)=="public"||program.get(index2)=="class"){
								break;
							}
							if(program.get(index2)==";"){//exoume declaration
								//System.out.println("MPAINEI "+program.get(index2));
								String name=program.get(index2-1);
								String type=program.get(index2-2);
								System.out.println(klash+"."+name+" : "+indexforvars);
								if(type=="int"){
									indexforvars+=4;
								}
								else if(type=="boolean"){
									indexforvars+=1;
								}
								else{
									indexforvars+=8;
								}
								
							}
							index2++;
						}
						int index3=index2;
						while(index3!=size){
							if(program.get(index3)=="class"){
								break;
							}
							if(program.get(index3)=="public"){
								String funname=program.get(index3+2);
								int flagfun=0;
								if(superclass!=";"){//an yparxei superclass
									int indexsuperclass=0;
									
									while(indexsuperclass!=size){
										if(program.get(indexsuperclass)=="class"&&program.get(indexsuperclass+1)==superclass){
												
											int indexforfun=indexsuperclass;
											indexforfun++;
											while(indexforfun!=size&&program.get(indexforfun)!="class"){
												//System.out.println("AAAAAAAAAAAa "+program.get(indexforfun));
												if(program.get(indexforfun)=="public"&&program.get(indexforfun+2)==funname){//yparxei h fun sthn hyprclass
													flagfun=1;
													break;
												}
												indexforfun++;
											}
											break;
										}
										indexsuperclass++;
									}								
								}
								if(flagfun==0){//an den yparxei se yperklash
									System.out.println(klash+"."+funname+" : "+indexforpointers);
									indexforpointers+=8;
								}
								//break;
							}
							index3++;
							//System.out.println("edwwww  "+index3);
						}
					}
					index++;
				}
			}


			}
			catch(ParseException ex){
				System.out.println(ex.getMessage());
			}
			catch(FileNotFoundException ex){
				System.err.println(ex.getMessage());
			}
			finally{
				if(file==";"){
					System.out.println("\nFile "+args[i]+" Couldn't parse correctly-PARSEERROR");
				}
				try{
					if(fis != null) {
						fis.close();
						
					}
				}
				catch(IOException ex){
					System.err.println(ex.getMessage());
					
				}
				continue;
			}
		
		}
		
	
    }
}
