package com.example.dehab.sol_contract_wrapper;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.2.0.
 */
public class MultiSignatureWallet extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50604051604080610770833981016040908152815160209283015160008054600160a060020a03191633178155600160a060020a039283168152600194859052838120805460ff19908116871790915592909116815291909120805490911690911790556106ed806100836000396000f3006080604052600436106100765763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041662e9c006811461007b578063012da1a3146100955780635d9ec210146100bc578063d0e30db0146100d4578063d11db83f146100dc578063d4822fbf14610141575b600080fd5b34801561008757600080fd5b50610093600435610165565b005b3480156100a157600080fd5b506100aa6102d4565b60408051918252519081900360200190f35b3480156100c857600080fd5b506100936004356102da565b610093610425565b3480156100e857600080fd5b506100f1610497565b60408051602080825283518183015283519192839290830191858101910280838360005b8381101561012d578181015183820152602001610115565b505050509050019250505060405180910390f35b34801561014d57600080fd5b50610093600435600160a060020a0360243516610560565b600080548190600160a060020a031633148061019457503360009081526001602081905260409091205460ff16145b15156101d8576040805160e560020a62461bcd02815260206004820152601b60248201526000805160206106a2833981519152604482015290519081900360640190fd5b5060009050805b60045481101561025e578160ff166001141561023157600480548290811061020357fe5b906000526020600020015460046001830381548110151561022057fe5b600091825260209091200155610256565b600480548290811061023f57fe5b906000526020600020015483141561025657600191505b6001016101df565b60048054600019810190811061027057fe5b6000918252602082200155600480549061028e906000198301610664565b50505060009081526003602081905260408220805473ffffffffffffffffffffffffffffffffffffffff1990811682556001820180549091169055600281018390550155565b30315b90565b60008054600160a060020a031633148061030757503360009081526001602081905260409091205460ff16145b151561034b576040805160e560020a62461bcd02815260206004820152601b60248201526000805160206106a2833981519152604482015290519081900360640190fd5b5060008181526003602052604090208054600160a060020a0316151561037057600080fd5b33600090815260048201602052604090205460ff166001141561039257600080fd5b3360009081526004820160205260409020805460ff1916600190811790915560038201805490910190819055600211610421576002810154303110156103d757600080fd5b60018101546002820154604051600160a060020a039092169181156108fc0291906000818181858888f19350505050158015610417573d6000803e3d6000fd5b5061042182610165565b5050565b600054600160a060020a031633148061045157503360009081526001602081905260409091205460ff16145b1515610495576040805160e560020a62461bcd02815260206004820152601b60248201526000805160206106a2833981519152604482015290519081900360640190fd5b565b600054606090600160a060020a03163314806104c657503360009081526001602081905260409091205460ff16145b151561050a576040805160e560020a62461bcd02815260206004820152601b60248201526000805160206106a2833981519152604482015290519081900360640190fd5b600480548060200260200160405190810160405280929190818152602001828054801561055657602002820191906000526020600020905b815481526020019060010190808311610542575b5050505050905090565b600054600160a060020a031633148061058c57503360009081526001602081905260409091205460ff16145b15156105d0576040805160e560020a62461bcd02815260206004820152601b60248201526000805160206106a2833981519152604482015290519081900360640190fd5b3031821115610629576040805160e560020a62461bcd02815260206004820152601660248201527f416d6f756e7420697320696e73756666696369656e7400000000000000000000604482015290519081900360640190fd5b604051600160a060020a0382169083156108fc029084906000818181858888f1935050505015801561065f573d6000803e3d6000fd5b505050565b81548183558181111561065f5760008381526020902061065f9181019083016102d791905b8082111561069d5760008155600101610689565b5090560053656e646572206973206e6f7420612076616c6964206f776e65720000000000a165627a7a72305820e72dae85ae36b90df3c7e65a64ff8cfbc738055ca3e44997a4bd929a2b20a2780029";

    public static final String FUNC_DELETETRANSACTION = "deleteTransaction";

    public static final String FUNC_GETWALLETBALLANCE = "getWalletBallance";

    public static final String FUNC_SIGNTRANSACTION = "signTransaction";

    public static final String FUNC_DEPOSIT = "deposit";

    public static final String FUNC_GETPENDINGTRANSACTIONS = "getPendingTransactions";

    public static final String FUNC_TRANSFERTO = "transferTo";

    @Deprecated
    protected MultiSignatureWallet(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected MultiSignatureWallet(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected MultiSignatureWallet(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected MultiSignatureWallet(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<TransactionReceipt> deleteTransaction(BigInteger transactionId) {
        final Function function = new Function(
                FUNC_DELETETRANSACTION, 
                Arrays.<Type>asList(new Uint256(transactionId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getWalletBallance() {
        final Function function = new Function(FUNC_GETWALLETBALLANCE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> signTransaction(BigInteger transactionId) {
        final Function function = new Function(
                FUNC_SIGNTRANSACTION, 
                Arrays.<Type>asList(new Uint256(transactionId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> deposit(BigInteger weiValue) {
        final Function function = new Function(
                FUNC_DEPOSIT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<List> getPendingTransactions() {
        final Function function = new Function(FUNC_GETPENDINGTRANSACTIONS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteCall<List>(
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteCall<TransactionReceipt> transferTo(BigInteger amount, String receiverAddress) {
        final Function function = new Function(
                FUNC_TRANSFERTO, 
                Arrays.<Type>asList(new Uint256(amount),
                new org.web3j.abi.datatypes.Address(receiverAddress)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static MultiSignatureWallet load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new MultiSignatureWallet(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static MultiSignatureWallet load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new MultiSignatureWallet(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static MultiSignatureWallet load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new MultiSignatureWallet(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static MultiSignatureWallet load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new MultiSignatureWallet(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<MultiSignatureWallet> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String owner1, String owner2) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(owner1), 
                new org.web3j.abi.datatypes.Address(owner2)));
        return deployRemoteCall(MultiSignatureWallet.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<MultiSignatureWallet> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String owner1, String owner2) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(owner1), 
                new org.web3j.abi.datatypes.Address(owner2)));
        return deployRemoteCall(MultiSignatureWallet.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<MultiSignatureWallet> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String owner1, String owner2) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(owner1), 
                new org.web3j.abi.datatypes.Address(owner2)));
        return deployRemoteCall(MultiSignatureWallet.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<MultiSignatureWallet> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String owner1, String owner2) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(owner1), 
                new org.web3j.abi.datatypes.Address(owner2)));
        return deployRemoteCall(MultiSignatureWallet.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }
}
