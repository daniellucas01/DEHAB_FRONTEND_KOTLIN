package com.example.dehab.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dehab.Constants
import com.example.dehab.MainViewModel
import com.example.dehab.databinding.FragmentHomeBinding
import com.example.dehab.repository.WalletSingleton
import com.example.dehab.sol_contract_wrapper.MultiSignatureWallet
import com.example.dehab.ui.home.wallet_card.WalletCardAdapter
import com.example.dehab.ui.home.wallet_card.WalletCardItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.web3j.crypto.CipherException
import org.web3j.crypto.Credentials
import org.web3j.utils.Convert
import java.io.IOException
import java.math.BigDecimal


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: MainViewModel
    private lateinit var walletArrayList: ArrayList<WalletCardItems>
    private lateinit var _binding : FragmentHomeBinding
    private val binding get() = _binding
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var walletAddress: String


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        walletArrayList = ArrayList()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.walletData.observe(requireActivity(), Observer {
            Log.e(Constants.AUTHOR_NAME, "Observing" )
            walletArrayList = ArrayList()
            walletArrayList.add(
                (WalletCardItems(
                    Constants.PERSONAL_WALLET,
                    it.ether_count.toDouble(),
                    convertEtherToDollar(it.ether_count))
                        )
            )
            binding.walletRecyclerView.adapter = WalletCardAdapter(walletArrayList, requireActivity())
        })
        gridLayoutManager = GridLayoutManager(requireContext(), 1, LinearLayoutManager.VERTICAL, false)
        binding.walletRecyclerView.layoutManager = gridLayoutManager
        binding.walletRecyclerView.setHasFixedSize(true)
        binding.walletRecyclerView.adapter = WalletCardAdapter(walletArrayList, requireActivity())
        binding.setupWalletButton.setOnClickListener() {
            deployContract()
        }
        binding.depositEtherButton.setOnClickListener(){
            transfer1Ether()
        }
        binding.initWalletButton.setOnClickListener(){
            initWallet()
        }
    }

    private fun transfer1Ether() {
        CoroutineScope(Dispatchers.IO).launch {
            val oneEther = Convert.toWei("1", Convert.Unit.ETHER).toBigInteger();
            val walletContract = MultiSignatureWallet.load(
                "0xad14ced6364f30f6f575e184bf6841f89254c8be",
                WalletSingleton.web3,
                WalletSingleton.walletCredentials,
                WalletSingleton.gasPrice,
                WalletSingleton.gasLimit
            ).deposit(oneEther).send()
            withContext(Dispatchers.Main) {
                homeViewModel.setWalletCredentials(WalletSingleton.walletCredentials)
                homeViewModel.walletData.observe(requireActivity(), Observer {
                    Log.e(Constants.AUTHOR_NAME, "Observing" )
                    walletArrayList = ArrayList()
                    walletArrayList.add(
                        (WalletCardItems(
                            Constants.PERSONAL_WALLET,
                            it.ether_count.toDouble(),
                            convertEtherToDollar(it.ether_count))
                                )
                    )
                    binding.walletRecyclerView.adapter = WalletCardAdapter(walletArrayList, requireActivity())
                })
                Log.e(Constants.AUTHOR_NAME, "Deploy Success")
            }
        }
    }

    private fun initWallet() {
        CoroutineScope(Dispatchers.IO).launch {
            val walletContract = MultiSignatureWallet.load(
                "0xad14ced6364f30f6f575e184bf6841f89254c8be",
                WalletSingleton.web3,
                WalletSingleton.walletCredentials,
                WalletSingleton.gasPrice,
                WalletSingleton.gasLimit
            )
            val balance = walletContract.walletBallance.send()
            withContext(Dispatchers.Main) {
                val etherInDouble = Convert.fromWei(balance.toString(), Convert.Unit.ETHER)
                walletArrayList.add(
                    (WalletCardItems(
                        Constants.CONTRACT_WALLET,
                        etherInDouble.toDouble(),
                        convertEtherToDollar(etherInDouble))
                            )
                )
                binding.walletRecyclerView.adapter = WalletCardAdapter(walletArrayList, requireActivity())
                Log.e(Constants.AUTHOR_NAME, "Deploy Success")
            }
        }
    }

    private fun deployContract() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                //val web3j = Web3j.build(HttpService("https://rinkeby.infura.io/v3/1c87174ef80345fb92acbd2da20133e5"))
                val privateKey = "f15fb2252bd1836dd7cad56089bf3f83228c4c881b031f855af47acb611a6adc"
//                Log.e(Constants.AUTHOR_NAME, web3ClientVersion.web3ClientVersion)
                //val credentials = Credentials.create("ff2a6e67eba936f9e69a78dd6291a297c110c91b1fc76309ab7484b52793c0e2")
                val credentials = Credentials.create(privateKey)
                //val tManager = RawTransactionManager(web3j, credentials, )
//                val address1 = "0x70315d1643e3d7aee4685008b0b6f8407eb1e1fc"
                val address1 = "0x7919e12AfBae378f21659A4e2914a2B493211Fa3"
//                val address2 = "0xE771C2756b1EaC581cc8Bf146912785eB3d593eE"
                val address2 = "0xE771C2756b1EaC581cc8Bf146912785eB3d593eE"
//                Log.e(Constants.AUTHOR_NAME, "Checkpoint2")
//                val contract = MultiSignatureWallet.deploy(
//                    web3j,
//                    credentials,
//                    Contract.GAS_PRICE,
//                    Contract.GAS_LIMIT,
//                    address1,
//                    address2
//                ).send()
//                Log.e(Constants.AUTHOR_NAME, contract.contractAddress)
//                Log.e(Constants.AUTHOR_NAME, contract.isValid.toString())
                val contract = MultiSignatureWallet.deploy(
                    WalletSingleton.web3,
                    WalletSingleton.walletCredentials,
//                    BigInteger.valueOf(20000000000L),
//                    BigInteger.valueOf(6721975L),
                    WalletSingleton.gasPrice,
                    WalletSingleton.gasLimit,
                    address1,
                    address2
                ).send()
                Log.e(Constants.AUTHOR_NAME, contract.contractAddress)
                walletAddress = contract.contractAddress



                val walletContract = MultiSignatureWallet.load(
                    walletAddress,
                    WalletSingleton.web3,
                    credentials,
                    WalletSingleton.gasPrice,
                    WalletSingleton.gasLimit
                )
                Log.e(Constants.AUTHOR_NAME, walletContract.isValid.toString())
            }

            catch ( e : Exception) {
                Log.e(Constants.AUTHOR_NAME, e.toString())
            }

            catch ( e : CipherException) {
                Log.e(Constants.AUTHOR_NAME, e.toString())
            }

            catch ( e : IOException) {
                Log.e(Constants.AUTHOR_NAME, e.toString())
            }


            withContext(Dispatchers.Main) {
//                val contractAddress = contract.contractAddress
//                Log.e(Constants.AUTHOR_NAME, contractAddress)

                Log.e(Constants.AUTHOR_NAME, "Deploy Success")
            }
        }
    }

    private fun convertEtherToDollar (etherCount: BigDecimal) : Double {
        return (etherCount.toDouble()) * 238.88
    }
}
