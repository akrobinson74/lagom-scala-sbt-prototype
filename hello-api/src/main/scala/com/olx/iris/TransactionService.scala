package com.olx.iris

import java.time.ZonedDateTime

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import com.olx.iris.CustomerType.CustomerType

trait TransactionService extends Service {

  def getTransaction(transactionId: String): ServiceCall[NotUsed, Transaction]
  def processTransaction(): ServiceCall[Transaction, String]

  override final def descriptor: Descriptor = {
    import Service._
    named("transaction").withCalls(
      restCall(Method.POST, "/transactions", processTransaction _),
      restCall(Method.GET, "/transactions/:id", getTransaction _)
    )
  }
}

case class Transaction(
  transactionReference: String,
  customer: Customer,
  payments: List[Payment],
  products: List[Product]
)

case class Customer(
  address: Address,
  businessName: Option[String],
  customerType: CustomerType,
  emailAddress: String,
  firstName: String,
  language: String,
  lastName: String,
  userId: String,
  vatData: VatData
)

case class Address(
  city: String,
  country: String,
  houseNumber: String,
  region: String,
  street: String,
  zipCode: String
)

object CustomerType extends Enumeration {
  type CustomerType = Value
  val BUSINESS, RESIDENTIAL = Value
}

case class VatData(
  applyVat: Boolean = false,
  vatNumber: Option[String]
)

case class Payment(
  amount: MonetaryAmount,
  executionTime: ZonedDateTime
)

case class MonetaryAmount(
  currency: String,
  amount: BigDecimal
)

case class Product(
  currency: String = "CRD",
  description: String,
  discountAmount: Option[BigDecimal],
  expiryDateTime: Option[ZonedDateTime],
  grossPrice: BigDecimal = BigDecimal(0.00),
  netPrice: Option[BigDecimal],
  productType: ProductType,
  unitPrice: BigDecimal = BigDecimal(1.00),
  units: BigInt,
  vatAmount: Option[BigDecimal],
  vatPercentage: BigDecimal = BigDecimal(0.00)
)

sealed class ProductType(fiqasTransactionType: String)

object EFFORT extends ProductType("Reward")

object REWARD extends ProductType("Reward")

object TOPUP extends ProductType("WalletTopUp")