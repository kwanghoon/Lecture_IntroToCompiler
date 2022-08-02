# 4. Arith 프로그램을 VM 프로그램으로 컴파일하기

지금까지  소스프로그램과 타겟프로그램을  각각 추상구문트리로  작성하는
방법을 살펴보았다. 이제  소스프로그램을 타겟프로그램으로 컴파일 변환할
준비가 되었다.

Arith 컴파일러는 소스프로그램의 추상구문트리를 입력받아 타겟프로그램의
추상구문트리를 출력하는 함수로 작성한다.

 - 소스프로그램의 추상구문트리 : `ArrayList<Expr>`
 - 타겟프로그램의 추상구문트리 : `ArrayList<Instr>`

이름이   동일한(overloading된)   두  개의   compile함수로   컴파일러를
작성해보자.

 - 세미콜론으로 분리된 식 리스트를 입력받아 명령어 리스트를 출력하는 함수
 - 하나의 식을 입력받아 명령어 리스트를 출력하는 함수

```java
ArrayList<Instr> compile(ArrayList<Expr> exprSeq) {
    ArrayList<Instr> instrs = new ArrayList<Instr>();
		
	int index = 0;
	while (index < exprSeq.size()) {
		ArrayList<Instr> subInstrs = compile(exprSeq.get(index));
			
		instrs.addAll(subInstrs);
		instrs.add(new Pop());
			
		index = index + 1;
	}
	
	return instrs;
}

ArrayList<Instr> compile(Expr expr) {
	ArrayList<Instr> instrs = new ArrayList<Instr>();
	if (expr instanceof BinOp) {
		BinOp binOpExpr = (BinOp)expr;
			
		ArrayList<Instr> leftInstrs = compile(binOpExpr.getLeft());
		ArrayList<Instr> rightInstrs = compile(binOpExpr.getRight());
			
		instrs.addAll(leftInstrs);
		instrs.addAll(rightInstrs);
			
		switch(binOpExpr.getOpKind()) {
		case BinOp.ADD:
			instrs.add(new InstrOp(InstrOp.ADD));
			break;
		case BinOp.SUB:
			instrs.add(new InstrOp(InstrOp.SUB));
			break;
		case BinOp.MUL:
			instrs.add(new InstrOp(InstrOp.MUL));
			break;
		case BinOp.DIV:
			instrs.add(new InstrOp(InstrOp.DIV));
			break;
		}
	} else if (expr instanceof Assign) {
		Assign assignExpr = (Assign)expr;
			
		String varName = assignExpr.getVarName();
		Expr rhs = assignExpr.getRhs();
			
		ArrayList<Instr> rhsInstrs = compile(rhs);
			
		instrs.addAll(rhsInstrs);
		instrs.add(new Store(varName));
		instrs.add(new Push(varName));
			
	} else if (expr instanceof Lit) {
		Lit litExpr = (Lit)expr;
			
		Integer intLitV = litExpr.getInteger();
			
		instrs.add(new Push(intLitV));
	} else if (expr instanceof Var) {
		Var varExpr = (Var)expr;
			
		String varName = varExpr.getVarName();
			
		instrs.add(new Push(varName));
	}
	return instrs;
}
```
컴파일 과정을 예를 들어 살펴보자.

 - `new Var("x")`  ===>  `new Push("x")`
 
 - `new Lit(123)`  ===>  `new Push(123)`
 
 - `new Assign("x", 123)`

    ===>

    ```java
    new Push(123)
    new Store("x")
	new Push("x")
	```

 - `new BinOp(BinOp.ADD, "x", 123)`

    ===>
    ```java
    new Push("x")
    new Push(123)
    new InstrOp(InstrOp.ADD)
    ```

참고로  위  예제에서는 이해하기  쉬운  형태로  다소 부정확하게  작성한
것이다.   컴파일  변환으로  얻은  타겟프로그램  추상구문트리를  정확히
작성하려면  `ArrayList<Instr>`  객체를   만들어  나열한  `Instr`  객체들을
리스트로 작성해야 한다.


이제    컴파일러를    완성하였다.   그러면    주어진    소스프로그램의
추상구문트리를 컴파일 변환하여 타겟프로그램의 추상구문트리를 만들고 이
타겟프로그램을 가상기계를 통해 실행하는 과정을 쉽게 작성할 수 있다.

```java
ArrayList<Expr> exprSeq = ... 소스프로그램 추상구문트리 ...

Expr.prettyPrint(exprSeq);

ArrayList<Instr> instrs = arith.comp.Compiler.compile(exprSeq);
		
Instr.prettyPrint(instrs);

HashMap<String,Integer> envVM = new HashMap<String,Integer>();

VM.run(instrs, envVM);
```

소스프로그램   추상구문트리가   `exprSeq`에  주어져   있다고   가정하자.
compile함수로  타겟프로그램  추상구문트리  `instrs`를  만들고,  초기환경
`envVM`을을 만들어 `run`함수로 이 타겟프로그램을 실행시킬 수 있다.