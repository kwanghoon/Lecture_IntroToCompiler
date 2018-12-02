pragma solidity ^0.4.8;

contract MyCoin {
    // 상태 변수 선언
    string public name; // 토큰 이름
    string public symbol; // 토큰 단위
    uint8 public decimals; // 소수점 이하 자릿수
    uint256 public totalSupply; // 토큰 총량
    mapping (address => uint256) public balanceOf; // 각 주소의 잔고
     
    // 이벤트 알림
    event Transfer(address indexed from, address indexed to, uint256 value);

    // 생성자
    function MyCoin(uint256 _supply, string _name, string _symbol, uint8 _decimals) {
        balanceOf[msg.sender] = _supply;
        name = _name;
        symbol = _symbol;
        decimals = _decimals;
        totalSupply = _supply;
    }
 
// 송금
    function transfer(address _to, uint256 _value) {
        // 부정 송금 확인
        if (balanceOf[msg.sender] < _value) throw;
        if (balanceOf[_to] + _value < balanceOf[_to]) throw;

        balanceOf[msg.sender] -= _value;
        balanceOf[_to] += _value;
        Transfer(msg.sender, _to, _value);
    }
}

// (1) 에스크로
contract Escrow {
    // (2) 상태 변수
    MyCoin public token; // 토큰
    uint256 public salesVolume; // 판매량
    uint256 public sellingPrice; // 판매 가격
    uint256 public deadline; // 기한
    bool public isOpened; // 에스크로 개시 플래그
    address public owner; // 소유자 주소
     
    // (3) 이벤트 알림
    event EscrowStart(uint salesVolume, uint sellingPrice, uint deadline, address beneficiary);
    event ConfirmedPayment(address addr, uint amount);
     
    // 소유자 한정 메서드용 수식자
    modifier onlyOwner() { if (msg.sender != owner) throw; _; }

    // (4) 생성자
    function Escrow (MyCoin _token, uint256 _salesVolume, uint256 _priceInEther) {
        token = MyCoin(_token);
        salesVolume = _salesVolume;
        sellingPrice = _priceInEther * 1 ether;

	owner = msg.sender; // 처음에 계약을 생성한 주소를 소유자로 한다
    }
     
    // (5) 이름 없는 함수(Ether 수령)
    function () payable {
        // 개시 전 또는 기한이 끝난 경우에는 예외 처리
        if (!isOpened || now >= deadline) throw;
         
        // 판매 가격 미만인 경우 예외 처리
        uint amount = msg.value;
        if (amount < sellingPrice) throw;
         
        // 보내는 사람에게 토큰을 전달하고 에스크로 개시 플래그를 false로 설정
        token.transfer(msg.sender, salesVolume);
        isOpened = false;
        ConfirmedPayment(msg.sender, amount);
    }
     
    // (6) 개시(토큰이 예정 수 이상이라면 개시)
    function start(uint256 _durationInMinutes) onlyOwner {
        if (token == address(0) || salesVolume == 0 || sellingPrice == 0 || deadline != 0) throw;
        if (token.balanceOf(this) >= salesVolume){
            deadline = now + _durationInMinutes * 1 minutes;
            isOpened = true;
            EscrowStart(salesVolume, sellingPrice, deadline, owner);  
        }
    }
     
    // (7) 남은 시간 확인용 메서드(분 단위)
    function getRemainingTime() constant returns(uint min) {
        if(now < deadline) {
            min = (deadline - now) / (1 minutes);
        }
    }
     
    // (8) 종료
    function close() onlyOwner {
        // 토큰을 소유자에게 전송
        token.transfer(owner, token.balanceOf(this));
        // 계약을 파기(해당 계약이 보유하고 있는 Ether는 소유자에게 전송
        selfdestruct(owner);
    }
}
