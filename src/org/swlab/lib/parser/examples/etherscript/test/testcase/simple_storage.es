
account{balance:10ether} owner;
account{balance:50ether} user1;

account{contract:"simple_storage.sol", by:owner} simplestorage("SimpleStorage");

simplestorage.set(123) {by:user1};
uint x;
x = simplestorage.get() {by:user1};

assert simplestorage.storedData == 123;
assert x == 123;

