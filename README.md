# Ethereum-util
这是一个帮助以太坊合约开发者，快速获取合约方法签名的小工具。当我们将合约开发完成后，需要js或者java调用时，需要合约的方法签名。当然现在使用web3能生成方法签名，但是我们在拼接方法签名参数的过程中仍然有不可避免的错误。使用ethSignUtil工具能一次性生成所有合约的方法签名，帮助开发者校对合约方法签名是否正确，而不需要使用remix将一个个方法在里面编写生成方法签名校对


# source abi File
* way1：[truffle](https://github.com/trufflesuite/truffle-contract)

* way2：[solcjs](https://github.com/ethereum/solc-js)


# Use the premise
* way1: truffle compile 

  - 生成build/contracts目录，以及所有的合约json文件
  
* way2: solc  <contract>.sol --bin --abi --optimize -o abis/

  - 将合约生成abi和bin文件，生成的文件在abis目录下

# Usage
* download: [ethSignUtil.jar](https://github.com/zhjgit/Ethereum-util/releases/download/ethSignUtil1.0/ethSignUtil.jar)


```java
java -jar ethSignUtil.jar
```

##### 重要提示！ 必须将ethSignUtil.jar 放在上述truffle compile后的build目录的同级目录，或者是solc生成abis目录的同级目录。其中abis目录是强行指定的目录名称


# Contact the author
* weixin_public_number: blockchain_do / 刻意链习
* weibo : 刻意链习
![关注:刻意链习](https://mmbiz.qpic.cn/mmbiz_jpg/4MfEpgamqxM3EMC3rkQlhd9f1kgKEaKiamjVj4NC2mWa7xgibgVEKeI6cT1kXqxSwEjEJgxKwcmREicT5283KqYGQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
