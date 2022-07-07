# 1. 컴파일러란 무엇인가

컴파일러는    소스프로그램(Source    program)을    타겟프로그램(Target
program)으로 변환하는 프로그램이다. 

일반적으로 C,  C++, Java,  Python, JavaScript와  같은 프로그래밍언어를
사용하여     프로그램을    작성한다.     이러한    프로그램을     보통
소스프로그램이라고 한다.

타겟프로그램은    보통   어셈블리어(Assembly)    또는   기계어(Machine
language)로 작성한다.  컴퓨터 중앙처리장치(CPU)에  따라 타겟프로그램을
작성하는데 사용하는 프로그래밍언어가 다르다.  예를 들어, 보통 데스크탑
컴퓨터에서  x86계열  중앙처리장치를  사용하는  경우  x86  어셈블리어로
타겟프로그램을  작성하고,   스마트폰의  경우   ARM계열  중엉처리장치를
사용하기    때문에    ARM   어셈블리어로    타겟프로그램을    작성하게
된다.   자바가상기계(Java    vitual   machine)는   자바바이트코드(Java
bytecode)로 타겟프로그램을 작성한다.

예를 들어, 아래와 같은 소스프로그램을 가정해보자. 나중에 소개할 장난감
프로그래밍언어 Arith로 작성한 프로그램이다.
```
   x = 123;
   x = x + 1;
   y = x; 
   y = y - 1 * 2 / 3;
   z = y = x
```
이  소스프로그램의  구문(syntax)과  의미(semantics)는  C,  C++,  Java,
Python,  JavaScript와   같은  프로그램언어   중  어느   하나라도  배운
사람이라면 쉽게 이해할 수 있을 것이다.

### Q. 위 소스프로그램의 구문을 설명하시오.

### Q. 위 소스프로그램의 의미를  설명하시오. 위 타겟프로그램을 실행한 다음, 변수 x,y,z의 값은 무엇인가?

또한  타겟프로그램의  예를  들어보자.   나중에  소개할  가상기계  VM의
명령어(Instruction)으로 작성하였다.
```
   PUSH 123
   STORE x
   PUSH x
   POP
   PUSH x
   PUSH 1
   ADD
   STORE x
   PUSH x
   POP
   PUSH x
   STORE y
   PUSH y
   POP
   PUSH y
   PUSH 1
   PUSH 2
   MUL
   PUSH 3
   DIV
   SUB
   STORE y
   PUSH y
   POP
   PUSH x
   STORE y
   PUSH y
   STORE z
   PUSH z
   POP
```
소스프로그램과 비교하면 구문과 의미를 이해하는데 조금 어려워졌다. 예제
타겟프로그램을 이해하려면 스택(stack)과 기억장치(memory)에 대한 기초를
알아야 한다.

 - PUSH 123은  스택에 123을  집어넣는다.
 - STORE x는 스택에서 숫자를 꺼내서 변수 x에 대입한다.
 - PUSH x는 변수 x에 저장된 숫자를 꺼내서 스택에 집어넣는다.
 - POP은 스택에서 숫자를 꺼내 버린다.
 - ADD는 스택에서 숫자 두개를 꺼내서  더한다. SUB, MUL, DIV도 비슷하게
   동작한다.

각  명령어의 구문과  의미를  이해했다면  위에서 소개한  타겟프로그램을
머리속으로 실행해 볼 수 있을 것이다.

Q. 위 타겟프로그램을 실행한 다음 변수 x,y,z의 값은 무엇인가?

사실  위의  타겟프로그램  예제는  소스프로그램  예제를  나중에  소개할
컴파일러를 통해서 컴파일하여 얻은 것이다.

일반적으로 컴파일 과정은 다음과 같다.

  소스프로그램
  
--> {구문 분석(Parsing)}

--> 소스프로그램 추상구문트리
  
--> {컴파일}

--> 타겟프로그램 추상구문트리

--> {프리티프린트(Pretty Print)} 

-->타겟프로그램

이렇게 얻어진 타겟프로그램을 가상기계 VM으로 실행한다.

위의 예제에 대한 컴파일 과정과 가상기계에서 실행하는 과정은 다음과 같다.

```
Parsing:
(x = 123);
(x = (x + 1));
(y = x);
(y = (y - ((1 * 2) / 3)));
(z = (y = x))

Compiling:
PUSH 123
STORE x
PUSH x
POP
PUSH x
PUSH 1
ADD
STORE x
PUSH x
POP
PUSH x
STORE y
PUSH y
POP
PUSH y
PUSH 1
PUSH 2
MUL
PUSH 3
DIV
SUB
STORE y
PUSH y
POP
PUSH x
STORE y
PUSH y
STORE z
PUSH z
POP

Running VM:

Environment:
x = 124
y = 124
z = 124

Successfully done.
```

Q. 소스프로그램에서 세미콜론으로 분리된 4개의 식이 타겟프로그램의 어느
   부분에 해당하는지 살펴보시오.




