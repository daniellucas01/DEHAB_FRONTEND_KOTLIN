package com.example.dehab.sol_contract_wrapper;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple4;
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
    private static final String BINARY = "608060405234801561001057600080fd5b50604051604080610980833981016040908152815160209283015160008054600160a060020a03191633178155600160a060020a039283168152600194859052838120805460ff19908116871790915592909116815291909120805490911690911790556108fd806100836000396000f3006080604052600436106100815763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041662e9c0068114610086578063012da1a3146100a05780635d9ec210146100c7578063d0e30db0146100df578063d11db83f146100e7578063d263b0591461014c578063d4822fbf14610196575b600080fd5b34801561009257600080fd5b5061009e6004356101ba565b005b3480156100ac57600080fd5b506100b5610329565b60408051918252519081900360200190f35b3480156100d357600080fd5b5061009e60043561032f565b61009e61047a565b3480156100f357600080fd5b506100fc6104ec565b60408051602080825283518183015283519192839290830191858101910280838360005b83811015610138578181015183820152602001610120565b505050509050019250505060405180910390f35b34801561015857600080fd5b506101646004356105b5565b60408051600160a060020a03958616815293909416602084015282840191909152606082015290519081900360800190f35b3480156101a257600080fd5b5061009e600435600160a060020a0360243516610668565b600080548190600160a060020a03163314806101e957503360009081526001602081905260409091205460ff16145b151561022d576040805160e560020a62461bcd02815260206004820152601b60248201526000805160206108b2833981519152604482015290519081900360640190fd5b5060009050805b6004548110156102b3578160ff166001141561028657600480548290811061025857fe5b906000526020600020015460046001830381548110151561027557fe5b6000918252602090912001556102ab565b600480548290811061029457fe5b90600052602060002001548314156102ab57600191505b600101610234565b6004805460001981019081106102c557fe5b600091825260208220015560048054906102e3906000198301610843565b50505060009081526003602081905260408220805473ffffffffffffffffffffffffffffffffffffffff1990811682556001820180549091169055600281018390550155565b30315b90565b60008054600160a060020a031633148061035c57503360009081526001602081905260409091205460ff16145b15156103a0576040805160e560020a62461bcd02815260206004820152601b60248201526000805160206108b2833981519152604482015290519081900360640190fd5b5060008181526003602052604090208054600160a060020a031615156103c557600080fd5b33600090815260048201602052604090205460ff16600114156103e757600080fd5b3360009081526004820160205260409020805460ff19166001908117909155600382018054909101908190556002116104765760028101543031101561042c57600080fd5b60018101546002820154604051600160a060020a039092169181156108fc0291906000818181858888f1935050505015801561046c573d6000803e3d6000fd5b50610476826101ba565b5050565b600054600160a060020a03163314806104a657503360009081526001602081905260409091205460ff16145b15156104ea576040805160e560020a62461bcd02815260206004820152601b60248201526000805160206108b2833981519152604482015290519081900360640190fd5b565b600054606090600160a060020a031633148061051b57503360009081526001602081905260409091205460ff16145b151561055f576040805160e560020a62461bcd02815260206004820152601b60248201526000805160206108b2833981519152604482015290519081900360640190fd5b60048054806020026020016040519081016040528092919081815260200182805480156105ab57602002820191906000526020600020905b815481526020019060010190808311610597575b5050505050905090565b600080548190819081908190600160a060020a03163314806105ea57503360009081526001602081905260409091205460ff16145b151561062e576040805160e560020a62461bcd02815260206004820152601b60248201526000805160206108b2833981519152604482015290519081900360640190fd5b505050600092835250506003602081905260409091208054600182015460028301549290930154600160a060020a03918216949390911692565b600061067261086c565b600054600160a060020a031633148061069e57503360009081526001602081905260409091205460ff16145b15156106e2576040805160e560020a62461bcd02815260206004820152601b60248201526000805160206108b2833981519152604482015290519081900360640190fd5b303184111561073b576040805160e560020a62461bcd02815260206004820181905260248201527f436f6e74726163742062616c616e636520697320696e73756666696369656e74604482015290519081900360640190fd5b600280546001808201835533808552600160a060020a0387811660208088018281526040808a018d815260006060808d018281528b835260038088528584208f518154908c1673ffffffffffffffffffffffffffffffffffffffff199182161782559751818e01805491909c1698169790971790995592519b85019b909b55905192909501919091556004805496870181559093527f8a35acfbc15ff81a39ae7d344fd709f28e8600b4aa8c65c6b64bfe7fe36bd19b909401859055815192835292820192909252808201889052928301829052519093507f41d8c89c4a25824354aa1f198d80d06f2f93f903a2d180d1ea20429a4678c4a49181900360800190a150505050565b81548183558181111561086757600083815260209020610867918101908301610893565b505050565b60408051608081018252600080825260208201819052918101829052606081019190915290565b61032c91905b808211156108ad5760008155600101610899565b5090560053656e646572206973206e6f7420612076616c6964206f776e65720000000000a165627a7a723058203f7f574a91ce611170332070eba778ff06674ec0ffd81ac76d4adbf4136b05570029";

    public static final String FUNC_DELETETRANSACTION = "deleteTransaction";

    public static final String FUNC_GETWALLETBALLANCE = "getWalletBallance";

    public static final String FUNC_SIGNTRANSACTION = "signTransaction";

    public static final String FUNC_DEPOSIT = "deposit";

    public static final String FUNC_GETPENDINGTRANSACTIONS = "getPendingTransactions";

    public static final String FUNC_GETPENDINGTRANSACTIONINFORMATION = "getPendingTransactionInformation";

    public static final String FUNC_TRANSFERTO = "transferTo";

    public static final Event TRANSACTIONCREATED_EVENT = new Event("TransactionCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TRANSACTIONCOMPLETED_EVENT = new Event("TransactionCompleted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TRANSACTIONSIGNED_EVENT = new Event("TransactionSigned", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

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

    public RemoteCall<Tuple4<String, String, BigInteger, BigInteger>> getPendingTransactionInformation(BigInteger transactionId) {
        final Function function = new Function(FUNC_GETPENDINGTRANSACTIONINFORMATION, 
                Arrays.<Type>asList(new Uint256(transactionId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple4<String, String, BigInteger, BigInteger>>(
                new Callable<Tuple4<String, String, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple4<String, String, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<String, String, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> transferTo(BigInteger amount, String receiverAddress) {
        final Function function = new Function(
                FUNC_TRANSFERTO, 
                Arrays.<Type>asList(new Uint256(amount),
                new Address(receiverAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<TransactionCreatedEventResponse> getTransactionCreatedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSACTIONCREATED_EVENT, transactionReceipt);
        ArrayList<TransactionCreatedEventResponse> responses = new ArrayList<TransactionCreatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransactionCreatedEventResponse typedResponse = new TransactionCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.transactionId = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransactionCreatedEventResponse> transactionCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, TransactionCreatedEventResponse>() {
            @Override
            public TransactionCreatedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSACTIONCREATED_EVENT, log);
                TransactionCreatedEventResponse typedResponse = new TransactionCreatedEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.transactionId = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransactionCreatedEventResponse> transactionCreatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSACTIONCREATED_EVENT));
        return transactionCreatedEventFlowable(filter);
    }

    public List<TransactionCompletedEventResponse> getTransactionCompletedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSACTIONCOMPLETED_EVENT, transactionReceipt);
        ArrayList<TransactionCompletedEventResponse> responses = new ArrayList<TransactionCompletedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransactionCompletedEventResponse typedResponse = new TransactionCompletedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.transactionId = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransactionCompletedEventResponse> transactionCompletedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, TransactionCompletedEventResponse>() {
            @Override
            public TransactionCompletedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSACTIONCOMPLETED_EVENT, log);
                TransactionCompletedEventResponse typedResponse = new TransactionCompletedEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.transactionId = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransactionCompletedEventResponse> transactionCompletedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSACTIONCOMPLETED_EVENT));
        return transactionCompletedEventFlowable(filter);
    }

    public List<TransactionSignedEventResponse> getTransactionSignedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSACTIONSIGNED_EVENT, transactionReceipt);
        ArrayList<TransactionSignedEventResponse> responses = new ArrayList<TransactionSignedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransactionSignedEventResponse typedResponse = new TransactionSignedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.by = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.transactionId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransactionSignedEventResponse> transactionSignedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, TransactionSignedEventResponse>() {
            @Override
            public TransactionSignedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSACTIONSIGNED_EVENT, log);
                TransactionSignedEventResponse typedResponse = new TransactionSignedEventResponse();
                typedResponse.log = log;
                typedResponse.by = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.transactionId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransactionSignedEventResponse> transactionSignedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSACTIONSIGNED_EVENT));
        return transactionSignedEventFlowable(filter);
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
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(owner1),
                new Address(owner2)));
        return deployRemoteCall(MultiSignatureWallet.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<MultiSignatureWallet> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String owner1, String owner2) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(owner1),
                new Address(owner2)));
        return deployRemoteCall(MultiSignatureWallet.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<MultiSignatureWallet> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String owner1, String owner2) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(owner1),
                new Address(owner2)));
        return deployRemoteCall(MultiSignatureWallet.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<MultiSignatureWallet> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String owner1, String owner2) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(owner1),
                new Address(owner2)));
        return deployRemoteCall(MultiSignatureWallet.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class TransactionCreatedEventResponse {
        public Log log;

        public String from;

        public String to;

        public BigInteger amount;

        public BigInteger transactionId;
    }

    public static class TransactionCompletedEventResponse {
        public Log log;

        public String from;

        public String to;

        public BigInteger amount;

        public BigInteger transactionId;
    }

    public static class TransactionSignedEventResponse {
        public Log log;

        public String by;

        public BigInteger transactionId;
    }
}
