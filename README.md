# 컴파일러가 이렇게 쉬울 수가 - 하향식 방법으로 컴파일러 작성하기 (A Top-down approach to writing a compiler)

## 학습 목표
- 추상 구문 트리(Abstract syntax tree)를 이해하고 자료구조로 표현하기
- 낱말 분석(lexcical analysis)과 구문 분석(syntax analysis) 이해하기
- 추상 구문 트리를 입력으로 받는 프로그램을 작성하기 
- 추상 구문 트리를 출력으로 내는 프로그램을 작성하기
- 추상 구문 트리로 표현된 프로그램을 실행하기 (해석기 - interpreter)

### 목차
 1. 컴파일러란 무엇인가
 2. 프로그래밍언어 Arith의 구문과 의미
 3. 가상기계 VM 명령어 구문과 의미
 4. Arith 프로그램을 VM 프로그램으로 컴파일하기
 5. 프로그래밍언어 Arith 파서 만들기 (처음 공부할 때 건너뛰세요)
 6. 파서, 컴파일러, 가상기계를 합한 시스템
 7. 마무리


#### 추천 공부 코스: 1절 ~ 4절, 6절, 7절
 - 강의 내용 : doc/tutorial.txt
 - (5절은 파서를 만드는 내용으로 상대적으로 난이도가 높습니다.)

#### 참고
 - [YAPB](https://github.com/kwanghoon/yapb)을 윈도우용으로 미리 빌드한 바이너리를 사용하기 때문에 윈도우에서만 동작합니다 (genlrparser-exe.exe)

#### 작성자
- 전남대 소프트웨어공학과 최광훈
 
