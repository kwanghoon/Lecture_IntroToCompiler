# 2. 프로그래밍언어 Arith의 구문과 의미

프로그래밍언어 Arith의 구문을 먼저 서술식으로 설명한다.

 - Arith 프로그램은 식을 세미콜론으로 구분하여 나열한 것이다.

   예) x = 123 ; x = x + 1 ; z = 0; y * z ; y = x

 - 세미콜론으로  구분된  각각의  식은  산술식(arithmetic  expression),
   할당식(assignment   expression)형태이다.   할당식의  왼편은   변수,
   오른편은 임의의 식이 올 수 있다.

   예) x = 123 또는 y * z

 - 식을 구성하는 가장 기본식(primary expression)은 변수나 숫자이다.

 - 괄호를 사용하여 복잡한 식을 구성할 수 있다.

   예)  1 + (2 - 3) * 4 / 5

Arith 프로그래밍언어의 식을 Java로 작성할 수 있다.

 - `x = 123`
    
```java
	new Assign("x", new Lit(123))
```
 - `x = x + 1`
```java
	new Assign("x", 
		new BinOp(BinOp.ADD, new Var("x"), new Lit(1)))
```
 - `y - 1 * 2 / 3`

```java
	new BinOp(BinOp.SUB,
		new Var("y"),
		new BinOp(BinOp.DIV,
			new BinOp(BinOp.MUL, new Lit(1), new Lit(2)),
			new Lit(3)))
```
 - `x = 123 ; x = x + 1`
```java
	Expr[] exprs = {
		new Assign("x", new Lit(123)),
		new Assign("x",
			new BinOp(BinOp.ADD, new Var("x"), new Lit(1)))
   };
   
	exprSeq = new ArrayList<Expr>(Arrays.asList(exprs));
```

위에서 보여준  Java 코드를 **추상구문트리(Abstract syntax  tree)** 라 한다.
추상구문트리는   소스프로그램의   구문을   트리   자료구조(tree   data
structure)로 요약해서 표현한 것이다.

위 추상구문 트리를 위해서 Java 클래스들을 준비해두어야 한다.

 - arith.ast 패키지의 클래스들
    - `Expr`
    - `Assign extends Expr`
    - `BinOp extends Expr`
    - `Lit extends Expr`
    - `Var extends Expr`

 - 세미콜론으로  구분된   식들을  표현할  때   Java의  `ArrayList<Expr>`
   클래스를 사용한다.

### Q. 다음 식을 Java로 작성한 추상구문트리를 만들어보시오.

 - z = y

 - z + 123

다음 식을 Java로 작성한 추상구문트리를 만들때 주의할 점이 있다. 예를 들어,
1 + 2 * 3에 대한 추상구문트리를 
```java
	new BinOp(BinOp.MUL,
		new BinOp(BinOp.ADD, new Lit(1), new Lit(2)),
		new Lit(3))
```
와 같이 만들면 안된다. 정확한 추상구문트리는 아래와 같다.
```java
	new BinOp(BinOp.ADD,
		new Lit(1),
		new BinOp(BinOp.MUL, new Lit(2), new Lit(3)))
```
앞의  추상구문트리가 표현한  Arith  프로그래밍언어의 식은

 - (1 + 2) * 3이고,

뒤에서 표현한 식은

 - 1 + (2 * 3)이다.

소스프로그램에서  직접 괄호를  사용하여  (1 +  2)  * 3이라고  작성하면
덧셈을 하고 곱셈을 해야하지만 괄호가 없다면 먼저 곱셈을 한다.

추상구문트리에서 아래쪽(루트와 멀리 떨어져  있는 곳에) 위치한 부분부터
먼저 계산하는 것이 일반적이다.

C,  C++, Java,  Python,  JavaScript에서도  이렇게 해석하도록  정의되어
있는데 **연산자 우선순위(operator precedence) 규칙**이라고 부른다.

위의 예는 서로 다른 연산자 +와 *를 혼합해서 사용할 때 인데, 동일한 연산자를
여러번 사용하는 식을 추상구문트리로 작성할 때도 주의해야 한다.

1 - 2 - 3에 대한 추상구문트리를 다음과 같이

```java
	new BinOp(BinOp.SUB,
		new BinOp(BinOp.SUB,
			new Lit(1),
			new Lit(2)),
		new Lit(3))
```
작성하여  (1 -  2) -  3으로 왼쪽에서 차례로 뺄셈을 하도록 해석하는  것이 맞다.


하지만  x =  y =  z에 대한 추상구문트리는
```java
	new Assign("x",
		new Assign("y", Var("z")))
```
와 같이  작성하여 x  = (y  = z)로  해석한다. 오른쪽에서  차례로 변수에
대입하도록 해석한다. 즉,  변수 z의 값을 변수 y에 넣고,  대입한 그 값을
변수 x에 넣는다.

역시 C, C++, Java, Python, JavaScript에서도 이렇게 해석하도록 정의되어
있는데 **연산자 결합(operator associativity) 규칙** 이라고 부른다.

주어진  Arith  소스프로그램에   대한  추상구문트리를  작성하는  방법을
설명했다.  **Arith 소스프로그램의 의미(semantics)** 를 정의해보자.

해석기(interpreter)라고 하는  함수를 작성하여  프로그래밍언어의 의미를
정의할  수  있다.   이  함수의 입력은  추상구문트리이고,  출력은  실행
결과이다.  Arith언어의  경우 프로그램  실행이 종료되었을 때  각 변수에
들어 있는 값이 실행 결과이다.

 - 예) x = 123 ; x = x + 1 ; z = 0; y * z ; y = x

      위 프로그램의 의미는 변수 x는 124, z는 0, y도 124이다.

이렇게  어떤  변수가  어느  값을  가지고  있는지  보관하는  자료구조를
환경(environment)라고 부른다. 환경은 보통 아래와 같이 표기한다.

 - { x=124, y=124, z=0 }

Java의  HashMap<String,Integer> 클래스를  사용하면 Java로  쉽게 환경을
작성하여 다룰 수 있다.

```java
HashMap<String,Integer> env = new HashMap<String,Integer>();
   env.put("x", 124);
   env.put("y", 124);
   env.put("z", 0);
```

 - `env.get("x")`는 124가 될 것이다.

프로그램언어 Arith의 해석기를 `Interp` 클래스의 `seq`함수와 `expr`함수로 작성해보자.

`seq`함수는 세미콜론으로 구분된 식들을 받아 **환경을 변경하여 결과** 로 내고,

`expr`함수는 하나의 식을 받아 **환경을 바꾸고 정수를 실행 결과** 로 반환한다.

`seq`함수는  `ArrayList<Expr>`   객체로  표현된  식들을  받아   각  식마다
`expr`함수를 호출하여 차례대로 실행하도록 작성한다.

```java
void seq(ArrayList<Expr> exprList, HashMap<String, Integer> env) {
	int index = 0;
		
	while (index < exprList.size()) {
		Integer retV = expr(exprList.get(index), env);
		index = index + 1;
	}
}
```
`expr`함수는  다음과 같이  작성한다.   
식을 Expr  객체로  받아, 이  식이 BinOp이면 산술계산,  Assign이면 변수에 값을 대입하여  환경을 변경하고,
Lit이면 상수, Var이면 환경 env에서 변수를 읽는 것을 실행한다.

```java
Integer expr(Expr expr, HashMap<String, Integer> env) {
	if (expr instanceof BinOp) {
		BinOp binOpExpr = (BinOp)expr;
			
		Integer leftV = expr(binOpExpr.getLeft(), env);
		Integer rightV = expr(binOpExpr.getRight(), env);
			
		switch(binOpExpr.getOpKind()) {
		case BinOp.ADD:
			return leftV + rightV;
		case BinOp.SUB:
			return leftV - rightV;
		case BinOp.MUL:
			return leftV * rightV;
		case BinOp.DIV:
			return leftV / rightV;

		}
	} else if (expr instanceof Assign) {
		Assign assignExpr = (Assign)expr;
			
		String varName = assignExpr.getVarName();
		Expr rhs = assignExpr.getRhs();
			
		Integer rhsV = expr(rhs, env);
		env.put(varName, rhsV);
			
		return rhsV;
	} else if (expr instanceof Lit) {
		Lit litExpr = (Lit)expr;
			
		Integer intLitV = litExpr.getInteger();
			
		return intLitV;
	} else if (expr instanceof Var) {
		Var varExpr = (Var)expr;
			
		String varName = varExpr.getVarName();
		Integer varV = env.get(varName);
		assert varV != null;
			
		return varV;
	}
}
```
프로그래밍언어 Arith의 해석기를 사용하여 주어진 소스프로그램을 실행하는
방법은 다음과 같다.
```java
ArrayList<Expr> exprSeq =  ... 추상구문트리 ...

HashMap<String,Integer> env = new HashMap<String,Integer>();
Interp.seq(exprSeq, env);
```
exprSeq는 세미콜론으로  구분된 식들의 리스트에  대한 추상구문트리이다.
실행을 위해서 비어있는 환경 env를 만든다.

