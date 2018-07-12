package com.olx.iris
import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.ServiceCall

class TransactionServiceImpl extends TransactionService {
  override def getTransaction(transactionId: String): ServiceCall[NotUsed, Transaction] = {

  }

  override def processTransaction(): ServiceCall[Transaction, String] = ???
}
