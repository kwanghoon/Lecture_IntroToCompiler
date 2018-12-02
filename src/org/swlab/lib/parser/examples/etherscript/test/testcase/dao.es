
account{balance:50ether} owner;
account{balance:50ether} user1;
account{balance:50ether} user2;
account{balance:50ether} user3;
account{balance:50ether} hacker;

account{contract:"dao.sol", by:owner} dao("SimpleDAO");

dao.donate(user1) {by:user1, value:1ether};
dao.donate(user2) {by:user2, value:1ether};
dao.donate(user3) {by:user3, value:1ether};

account{contract:"dao.sol", by:hacker} mallory("Mallory", dao);

dao.donate(mallory) {by:hacker, value:100finney};

mallory.() {by:hacker};

mallory.getJackpot() {by:hacker};

assert owner.balance;
assert user1.balance;
assert user2.balance;
assert user3.balance;
assert hacker.balance;
