import visitor.GJDepthFirst;
import syntaxtree.*;
import java.util.*;
import java.io.IOException;

public class FirstVisitor extends GJDepthFirst<String, String>{
	int varcounter=0;
	int pointercounter=0;

	ArrayList<String> functions=new ArrayList<String>();

	ArrayList<String> classes=new ArrayList<String>();

	ArrayList<String> program=new ArrayList<String>();

	public ArrayList<String> ReturnOfArray(){
		return program;
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
      String _ret=null;
	  String name,klash;
	  n.f0.accept(this, argu);
	  program.add("class");
      klash=n.f1.accept(this, "main");
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
      n.f0.accept(this, argu);
	  program.add("class");
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
	  program.add("{");
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
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
      n.f0.accept(this, argu);
	  program.add("class");
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
	  program.add("extends");
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
	  program.add("{");
      n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      n.f7.accept(this, argu);
	  program.add("}");
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public String visit(VarDeclaration n, String argu) throws RuntimeException{
      String _ret=null;
	  n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
	  program.add(";");
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
   public String visit(MethodDeclaration n, String argu) {
      String _ret=null;
      n.f0.accept(this, argu);
	  program.add("public");
	  n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
	  program.add("(");
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
	  program.add(")");
      n.f6.accept(this, argu);
	  program.add("{");
      n.f7.accept(this, argu);
      n.f8.accept(this, argu);
      n.f9.accept(this, argu);
	  program.add("return");
      n.f10.accept(this, argu);
      n.f11.accept(this, argu);
	  program.add(";");
      n.f12.accept(this, argu);
	  program.add("}");
      return _ret;
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> FormalParameterTail()
    */
   public String visit(FormalParameterList n, String argu) {
      String _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public String visit(FormalParameter n, String argu) {
      String _ret=null;
	  n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
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
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> "boolean"
    * f1 -> "["
    * f2 -> "]"
    */
   public String visit(BooleanArrayType n, String argu) {
	  program.add("boolean[]");
      return "boolean[]";
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public String visit(IntegerArrayType n, String argu) {
	  program.add("int[]");
      return "int[]";
   }

   /**
    * f0 -> "boolean"
    */
   public String visit(BooleanType n, String argu) {
	  String bool= n.f0.accept(this, argu);
	  program.add(bool);
      return bool;
   }

   /**
    * f0 -> "int"
    */
   public String visit(IntegerType n, String argu) {
	  String integer=n.f0.accept(this, argu);
	  program.add(integer);
      return integer;
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
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
   public String visit(AssignmentStatement n, String argu) {
      String _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
	  program.add("=");
      n.f2.accept(this, argu);
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
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
	  program.add("[");
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
	  program.add("]");
      n.f4.accept(this, argu);
	  program.add("=");
      n.f5.accept(this, argu);
      n.f6.accept(this, argu);
	  program.add(";");
      return _ret;
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
      n.f0.accept(this, argu);
	  program.add("if");
      n.f1.accept(this, argu);
	  program.add("(");
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
	  program.add(")");
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
	  program.add("else");
      n.f6.accept(this, argu);
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
      n.f0.accept(this, argu);
	  program.add("while");
      n.f1.accept(this, argu);
	  program.add("(");
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
	  program.add(")");
      n.f4.accept(this, argu);
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
      n.f0.accept(this, argu);
	  program.add("System.out.println");
      n.f1.accept(this, argu);
	  program.add("(");
      n.f2.accept(this, argu);
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
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> Clause()
    * f1 -> "&&"
    * f2 -> Clause()
    */
   public String visit(AndExpression n, String argu) {
      String _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
	  program.add("&&");
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
   public String visit(CompareExpression n, String argu) {
      String _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
	  program.add("<");
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public String visit(PlusExpression n, String argu) {
      String _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
	  program.add("+");
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public String visit(MinusExpression n, String argu) {
      String _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
	  program.add("-");
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public String visit(TimesExpression n, String argu) {
      String _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
	  program.add("*");
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public String visit(ArrayLookup n, String argu) {
      String _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
	  program.add("[");
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
	  program.add("]");
      return _ret;
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
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
   public String visit(MessageSend n, String argu) {
      String _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
	  program.add(".");
	  n.f2.accept(this, argu);
      n.f3.accept(this, argu);
	  program.add("(");
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
	  program.add(")");
      return _ret;
   }

   /**
    * f0 -> Expression()
    * f1 -> ExpressionTail()
    */
   public String visit(ExpressionList n, String argu) {
      String _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
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
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> NotExpression()
    *       | PrimaryExpression()
    */
   public String visit(Clause n, String argu) {
      return n.f0.accept(this, argu);
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
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public String visit(IntegerLiteral n, String argu) {
	  String number; 
      number=n.f0.accept(this, argu);
	  program.add(number);
	  return number;
   }

   /**
    * f0 -> "true"
    */
   public String visit(TrueLiteral n, String argu) {
	  program.add("true");
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> "false"
    */
   public String visit(FalseLiteral n, String argu) {
	  program.add("false");
      return n.f0.accept(this, argu);
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public String visit(Identifier n, String argu) {
	  String id;
      id=n.f0.accept(this, argu);
	  program.add(id);
	  return id;
   }

   /**
    * f0 -> "this"
    */
   public String visit(ThisExpression n, String argu) {
	  program.add("this");
      return n.f0.accept(this, argu);
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
      return _ret;
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
      n.f0.accept(this, argu);
	  program.add("new");
      n.f1.accept(this, argu);
	  program.add("int");
      n.f2.accept(this, argu);
	  program.add("[");
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
	  program.add("]");
      return _ret;
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
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
	  program.add("(");
      n.f3.accept(this, argu);
	  program.add(")");
      return _ret;
   }

   /**
    * f0 -> "!"
    * f1 -> Clause()
    */
   public String visit(NotExpression n, String argu) {
      String _ret=null;
      n.f0.accept(this, argu);
	  program.add("!");
      n.f1.accept(this, argu);
      return _ret;
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
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
	  program.add(")");
      return _ret;
   }

}
