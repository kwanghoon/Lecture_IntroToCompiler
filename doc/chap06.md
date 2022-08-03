# 6. 파서, 컴파일러, 가상기계를 합한 시스템

지금까지 설명한 파서, 컴파일러, 가상기계를 모두 합하여 다음과 같은 시스템을
구성할 수 있다. 이 프로그램을 실행하면

 - 소스프로그램으로 사용할 파일 이름을 입력받고

 - Parsing 메소드로 소스프로그램의 추상구문트리를 만들고

 - compile 메소드로  VM 명령어로 구성된  타겟프로그램의 추상구문트리를
   만든 다음

 - run 메소드로 이 타겟프로그램의 추상구문트리를 받아 해석기로 실행한다.

```java
public class Main {
    public static void main(String[] args) {
	    Scanner scan = new Scanner(System.in);
	    String base = System.getProperty("user.dir");
	    String prj = "src/org/swlab/lib/parser/examples/arith/test";
	    File file = null;
		
	    while(true) {
            try {
                System.out.print("Enter your file name: ");
                String filename = scan.next();
                file = new File(base + "/" + prj + "/" + filename);
                FileReader fr = new FileReader(file);
                Parser parser = new Parser();
            
                ArrayList<Expr> exprSeq = (ArrayList<Expr>)parser.Parsing(fr);
                
                System.out.println("\nParsing:");
                Expr.prettyPrint(exprSeq);
                    
                ArrayList<Instr> instrs = (ArrayList<Instr>)Compiler.compile(exprSeq);
                    
                System.out.println("\nCompiling:");
                Instr.prettyPrint(instrs);
                    
                System.out.println("\nRunning VM:");
                HashMap<String,Integer> env = new HashMap<String,Integer>();
                VM.run(instrs, env);
                    
                System.out.println("\nEnvironment:");
                Set<String> vars = env.keySet();
                for(String var : vars) {
                    System.out.println(var + " = " + env.get(var));
                }
                
                System.out.println("\nSuccessfully done.");
		    }
		    catch(FileNotFoundException e) {
			    System.err.println("Not found: " + file);
		    } catch (IOException e) {
			    e.printStackTrace();
		    } catch (LexerException e) {
			    e.printStackTrace();
		    } catch (ParserException e) {
			    e.printStackTrace();
		    }
	    }
    }
}
```