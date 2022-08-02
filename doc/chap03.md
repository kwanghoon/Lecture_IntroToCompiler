# 3. 가상기계 VM 명령어 구문과 의미

이제  타겟프로그램을 작성할  때 사용하는  가상기계 VM  명령어의 구문과
의미를 살펴보자.   앞서 프로그램언어 Arith의 구문과  의미를 살펴보면서
기초 개념을 배웠는데 동일한 개념을 활용한다.


각 명령어를 표현하는 추상구문트리를 위해서 Java 클래스들을 정의하였다.

 - Instr
 - Push extends Instr
 - Pop extends Instr
 - InstrOp extends Instr
 - Store extends Instr

예를  들어,  왼편의  가상기계 명령어를  오른편과  같이  추상구문트리를
만든다.

 Push 2      Instr i1 = new Push(2);
 Push 1      Instr i2 = new Push(1);
 Store x     Instr i3 = new Store("x");
 Push x      Instr i4 = new Push("x");
 Add         Instr i5 = new InstrOp(InstrOp.ADD);
 Store y     Instr i6 = new Store("y");
 Push y      Instr i7 = new Push("y");
 Pop         Instr i8 = new Pop();

             Instr[] instrArr = {i1,i2,i3,i4,i5,i6,i7,i8};
             ArrayList<Instr> instrs
	       = new ArrayList<Instr>(Arrays.asList(instrArr);

각 명령어에 대해 동일한 이름의 Java 클래스를 작성하였다.

 - `Push` 클래스는 `Push(2)` 또는 `Push("x")`와 같이 숫자나 변수를 지정할 수
   있다.
 - `Store` 클래스는 변수를 지정한다.
 - 산술계산을 위한 명령어를 표현하기 위해  `ADD`, `SUB`, `MUL`, `DIV` 클래스가
   있다. 가상기계의  스택에 숫자를 꺼내 계산하기  때문에 별도의 인자를
   지정하지 않는다.
 - Pop 클래스에도 별도의 인자를 지정하지 않는다.

타겟프로그램은 이러한 클래스로 작성한 객체들을 Java의 `ArrayList<Instr>`
클래스로 순서대로 모아 리스트로 만들어 작성한다.

지금까지 가상기계 명령어로 작성한 타겟프로그램을 Java로 추상구문트리를
만드는  방법을 설명하였다.  이제  이 추상구문트리를  받아 각  명령어를
차례로 실행하여  최종적으로 환경을  결과로 내는  타겟프로그램의 의미를
정의해보자.  앞에서와 같이 타겟프로그램의 해석기 함수를 만든다.

가상기계 해석기는 `VM` 클래스의 `run`함수와 `interp`함수로 작성한다.

```java
void run(ArrayList<Instr> instrs, HashMap<String,Integer> env) {
	int index = 0;
	Stack<Integer> stack = new Stack<Integer>();
		
	while (index < instrs.size()) {
		interp(instrs.get(index), env, stack);
		index = index + 1;
	}
}


void interp(
   	Instr instr, HashMap<String,Integer> env, Stack<Integer> stack) {

	if (instr instanceof InstrOp) {
		InstrOp instrOp = (InstrOp)instr;
		Integer v2 = stack.pop();
		Integer v1 = stack.pop();
		switch(instrOp.getOpcode()) {
		case InstrOp.ADD:
			stack.push(v1 + v2);
			break;
		case InstrOp.SUB:
			stack.push(v1 - v2);
			break;
		case InstrOp.MUL:
			stack.push(v1 * v2);
			break;
		case InstrOp.DIV:
			stack.push(v1 / v2);
			break;
		}
	} else if (instr instanceof Push) {
		Push push = (Push)instr;
		Integer v;
			
		switch(push.getOperandKind()) {
		case Push.LIT:
			v = push.getIntLit();
			stack.push(v);
			break;
		case Push.VAR:
			String varName = push.getVarName();
			v = env.get(varName);
			assert v != null;
			stack.push(v);
			break;
		}
	} else if (instr instanceof Pop) {
		Pop pop = (Pop)instr;
		Integer v = stack.pop();
	} else if (instr instanceof Store) {
		Store store = (Store)instr;
		String varName = store.getVarName();
		Integer v = stack.pop();
		env.put(varName, v);
	} 
   }
```
이렇게  작성한  가상기계  해석기를  사용하여  타겟프로그램을  실행하는
방법은 다음과 같다.

 - ArrayList<Instr> instrs = ... 추상구문트리 ...
   HashMap<String,Integer> env = new HashMap<String,Integer>();
		
   VM.run(instrs, env);

instrs는  주어진  타겟프로그램의 추상구문트리이고,  해석기를  실행하기
전에 초기 환경 env를 만든다.

