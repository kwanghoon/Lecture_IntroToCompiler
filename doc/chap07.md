# 7. 마무리

이  튜토리얼 문서는  간단한 언어를  설계하고 해석기를  구현하는 방법을
공부하기 위한 예제로 적하하고, swlab 파서 라이브러리를 설명하는 문서로
사용할 수 있다.

## 7.1 swlab 파서 라이브러리와 파성 생성 도구와 비교

swlab 파서는 라이브러리 사용 접근 방법으로 파서를 만드는 특징을 가지고
있다.

보통 다른 파서 도구는 파서를 생성하는 접근 방법을 사용하고 있다. 즉,

  - 입력: 정규식 + 토큰 생성 코드 + 생산규칙 + 추상구문트리 생성 코드
  - 출력: `Parser.java` + `CommonParserUtil.java`에 있는 내용을 혼합한 파서 코드

특히 오토마톤 알고리즘에서 Reduce i 대신 Reduce 0부터 Reduce N으로 분류하고
(생산규칙의 전체 갯수가 N이라고 가정) Parser.java에서 작성한 각 생산규칙에
해당하는 추상구문트리 생성 코드를 직접 사용하는 형태이다.

그리고,   액션테이블과   고우투테이블을   배열  형태로   가지고   있을
것이다. 이때, 생산규칙들을 가지고 있는 생산규칙 테이블은 별도로 가지고
있지 않고 출력한 코드에서 생산규칙 테이블에서 얻고자 했던 정보인 RHS의
길이와 너터미널 심볼을 이미 참조한 코드가 포함되어 있을 것이다.

파서 라이브러리 접근 방법과 파성  생성 방법은 서로 장단점이 있다. 파서
생성 방법은 파서 프로그램을 C,  C++, Java와 같은 특정 프로그래밍언어로
생성하하는  방법은 비교적  'heavyweight'한데  비하여,  C, C++,  Java로
CommonParserUtil 모듈만 작성하면 파서 라이브러리 방법을 사용할 수 있기
때문에  상대적으로  'lightweight'하다.    참고로  Java로  작성한  파서
라이브러리에서  CommonParserUtil.java의 크기는  600라인 규모이다.   C,
C++,  Python, Haskell  등으로 만들때도  이 크기는  크게 늘어나지  않을
것으로  보인다.  따라서  각   프로그래밍언어  별로  파서  라이브러리를
준비해놓으면,  액션테이블,   고우투테이블,  생산규칙테입을을  생성하는
genlrparser.exe는  공유할  수  있기 때문에  파서를  작성하는데  사용할
프로그래밍언어의 선택이 넓어진다.

### 7.2 파서 라이브러리 구조

### 전체 구조

 - 파서 프로그래머가 작성하는 모듈 
 - 파서 라이브러리에서 제공하는 모듈

### 파서 프로그래머가 작성해야할 모듈

 - Token.*

    1) 토큰 상수들
    2) 각 토큰 상수를 문자열로 변환하는 함수

 - Lexer.*

    1) End Of Token에 해당하는 토큰 상수를 지정하기
    
    2) [ (정규식, 정규식에 매칭된 텍스트를 인자로 받고 토큰을 반환하는
                  함수) ]를 지정하기

 - Parser.*

    1) 시작 심볼 (Start symbol)
    2) [ (생산규칙, ()를 받아서 AST를 리턴하는 함수) ]
    
    3) Base Dir 각종 파일을 저장할 디렉토리 기준
    4) action table file name (e.g., action_table.txt)
    5) goto table file name (e.g., goto_table.txt)
    6) grammar file name (e.g., grammar.txt)
    7) parser spec file name (e.g., mygrammar.grm)

       2)의 생산규칙을 모아서 텍스트 파일로 만든 것
       오토마톤 생성 도구에 이 텍스트 파일을 입력으로 전달

          : 7) ==> 4),5),6)

    8) 오토마톤을 생성하는 도구 프로그램 이름 (e.g., genlrparser-exe)


### 파서 라이브러리에서 제공해야할 모듈

 - Token을 받아 String으로 변환하는 함수

   E.g., TokenInterface.java

         interface TokenInterface<Token> {
	    String toString(Token tok);
	 }

        
         TokenInterface.hs

         class TokenInterface token where
            fromToken  :: token -> String

 - CommonParserUtil

   : Lexer 스펙과 Parser 스펙을 받아서
     오토마톤 테이블을 생성하고
     이 테이블을 참조하여
     입력 파일로부터 읽은 텍스트를
     lexical/syntactic analysis 수행해서
     추상구문트리를 만든다.

   1) Lexer 파트

      (알고리즘)
      입력: lexer 스펙, lexing 대상 텍스트 
      출력: 터미널 리스트

      while (전체 텍스트 끝에 도달하지 않은 동안)
      
      1.  Lexer  스펙에서  현재 col/line  위치에서  시작하는  prefix와
         매치되는 정규식을 찾는다.

      2.  해당 정규식의  쌍으로 주어진  lexer 함수를  호출한다.  이때
          정규식에 매칭된  텍스트를 이  함수의 인자로  전달한다.  함수
          호출 결과 토큰을 반환한다.

      3. 현재 col/line/반환된 토큰/매칭된 텍스트 4가지 정보를 가지고
         terminal을 한 개 만든다.

      4. 이 terminal을 터미널 리스트 맨 뒤에 추가한다.

      5. 이 정규식에 매칭된 텍스트에 대해 col/line을 조정한다.

         while (매칭된 텍스트 끝에 도달하지 않은 동안)
	    - 현재 문자가 '\n' => line++; col=1;
	    - 그 이외 문자     => col++;

      참고) 순차적으로 찾는 과정을 최적화가 필요함.

   2) Parser 파트

      (알고리즘)
      입력: parser 스펙, 터미널 리스트 
      출력: AST

      1. paser 스펙으로부터 오토마톤을 생성한다.

       1-1. 파서 스펙이 변경되었는지 검사한다.
       1-2. 변경되었으면 오토마톤을 새로 생성한다.
       1-3. 변경되지 않았으면 이전에 생성한 오토마톤을 이용한다.

       참고) 파서 스펙의 변경 유무를 검사하는 방법

         Cond1 && Conde2 ==> 이전 오토마톤을 이용
	 otherwise       ==> 새로 오토마톤을 생성

         Cond1: action_table.txt, goto_table.txt, grammar.txt이 이 있는지 확인
	 
	 Cond2: 기존 mygrammar.grm의 내용과 주어진 파서 스펙으로 만들 새로운
	        mygrammar.grm의 내용이 같은지 확인.
		mygrammar.grm.hash 파일을 mygrammar.grm과 함께 생성해둔다.

      2. 생성된 3개의오토마톤 파일을 읽어들인다.
         prod_rules/action_table/goto_table.txt

         action_table ==>
	    hash_map { key : (state_string, token_string)
	               value : next_state_string }

            참고) token을 받아 string으로 받아주는 함수가 있었다!!
	    
         prod_rules ==>
	    [ lhs_string, [rhs_symbol_string] ]

            참고) 0: S' -> S
	          1: S -> C C
		  2: C -> c C
		  3: C -> d

             [ ("S'", ["S"])       <-- 0번 생산 규칙
	     , ("S",  ["C", "C"]   <-- 1번 생산 규칙
	     , ("C",  ["c", "C"]   <-- 2번 생산 규칙
	     , ("C",  ["d"]        <-- 3번 생산 규칙
	     ]

      3. 오토마톤을 이용해서 파싱을 진행

         참고) Stack의 원소
	         - Nonterminal :  ast를 갖고  있음 (디버깅  용으로 lhs
                   string을 갖고 있을 수도 있음)
		 - State : 상태를 문자열로 갖고 있음
		 - Terminal : 앞에서 설명한 4개 원소

         비어있는 스택 stack
	 
	 push "0" stack
       
         while ( end of token/terminal이 아닌 동안 반복 )

          현재 terminal과 스택 top에 있는 상태 state_top을
	  action table에서 찾는다.

          1) Shift state i :

               action table을 lookup해서 next state를 찾는다.
	       push next_state stack
	  
	  2) Reduce j prod_rule :

               production rule list를 looku해서 j번째 원소
	        (lhs_string, rhs_symbol_string_list)
	       를 찾으면

               rhs_symbol_string_list 길이의 2배 만큼 stack에서 pop

               현재 stack top의 상태와 lhs_string을 가지고
	       goto table을 lookup해서 그 다음 상태 next_state를 찾기

               j번째 생산규칙에 해당하는 parsing 함수를 호출한다.
	       이때 인자로, 앞에서 pop한 stack원소들 중 state들을 제외한
	       stack 원소들을 모으고 (스택이므로) 그 순서를 뒤집어서 리스트로
	       만든다. 이 리스트를 paring 함수에 전달한다.

               참고) 인자로 전달된 stack 원소들은 terminal 또는 nonterminal이다
	             nonterminal에 ast가 들어 있다.

               paring 함수 호출한 결과 ast를 반환 받는다.

               pop한 다음의 스택 top에 있는 state와 lhs_string을 가지고
	       goto table을 lookup해서 next_state를 찾는다.

               반환받은 ast를 stack에   push하고, next_state를 stack에 push한다.
	       (디버깅을 위해서 Nonterminal ast lhs_string을 고려할 수 있음)

	  3) Accept :
	       stack top에는 state가 있고, 그 바로 아래에 최종 ast를 갖고 있는
	       nonterminal이 있다.
	  
	  4) otherwise : 에러

--

 Java
   - Token.java
     Lexer.java
     Parser.java
     
   - TokenInterface.java
   
     CommonParserUtil.java
     
     StkElem.java
     Terminal.java
     Nonterminal.java
     ParseState.java

     TokenBuilder.java
     TreeBuilder.java
     
 Haskell
   - Token.hs
     Lexer.ha
     Parser.hs


   - TokenInterface.hs

     CommonParserutil.hs