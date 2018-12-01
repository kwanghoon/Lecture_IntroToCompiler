
account{balance:10ether} seller;
account{balance:50ether} customer;

account{contract:"coin.sol", by:seller} coin;
account{contract:"escrow.sol", by:seller} escrow(coin, 1, 5ether);

escrow.transfer(escrow,1) {by:seller};
escrow.start(60minutes) {by:seller};

escrow.() {by:customer, value:5ether};
escrow.close() {by:seller};

uint x;
x = coin.balancOf(customer) {by:customer};

assert x == 1;
assert seller.balance == 15ether;
assert customer.balance == 45ether;

