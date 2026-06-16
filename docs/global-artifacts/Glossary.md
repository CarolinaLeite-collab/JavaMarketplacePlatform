# MiteLovers - Domain Model Glossary

___
## Table of contents

- [Activity History](#activity-history)
- [Address](#address)
- [Appraisal](#appraisal)
- [Appraisal Entity](#appraisal-entity)
- [Auction](#auction)
- [Author](#author)
- [Bid](#bid)
- [Bidder](#bidder)
- [Binding](#binding)
- [Book](#book)
- [Buyer](#buyer)
- [City](#city)
- [Community Member](#community-member)
- [Condition](#condition)
- [Country](#country)
- [Direct Sale](#direct-sale)
- [Edition](#edition)
- [Genre](#genre)
- [ISBN](#isbn)
- [ISSN](#issn)
- [Item](#item)
- [Library](#library)
- [List of Items](#list-of-items)
- [Logistic Service](#logistic-service)
- [Magazine](#magazine)
- [Outright Price](#outright-price)
- [Payment Processing](#payment-processing)
- [Phone Prefix](#phone-prefix)
- [Post](#post)
- [Price](#price)
- [Publication](#publication)
- [Publication Type](#publication-type)
- [Publishing Company](#publishing-company)
- [Seller](#seller)
- [Shopping Cart](#shopping-cart)
- [Social Network](#social-network)
- [Support Service](#support-service)
- [Title](#title)
- [Transaction](#transaction)
- [User](#user)
- [User Rating](#user-rating)
- [User Role](#user-role)
- [Weight](#weight)

___

#### Activity History

The `Activity History` tracks and saves a `user`'s past actions or transactions (buying or selling) within the marketplace.

___

#### Address

Represents a physical `Address` with details such as street, door number, building type, `City`, district/state, `Country`, and postal code.
___

#### Appraisal

The `Appraisal` of an `Item`, including its estimated value, appraisal date, and description.

___


#### Appraisal Entity

A registered `user` that is responsible for evaluating the condition, authenticity, or value of an `item` before it is put up for sale.
Contains the appraisal entity's `Name`, the list of `PublicationTypeId`s it can appraise, and the list of `GenreId`s it specializes in.

___

#### Auction

 Represents a time-bounded selling mechanism where one or more `Item`(s) (via their `ItemId`(s)) are listed for sale via competitive bidding. 
 An auction is active only within its configured time window: `auctionStartDate` to `auctionEndDate`.
 `Bids` placed during the active period determine the `final sale price` and winning `buyer`.

The `seller` defines the following attributes for an `auction`:

- `startingPrice`: minimum price required for the first valid bid
- `reservePrice`: minimum acceptable price for the Item to be sold; bids below this price do not result in a sale
- `outrightPrice` (optional): a _buy now_ price that allows immediate purchase without waiting for the auction to end
- `auctionStartDate`: date and time when the auction becomes active
- `auctionEndDate`: date and time when the auction closes

Items **cannot** be simultaneously on `DirectSale` (e.g., in a `shopping cart`) and on `Auction`.

___

#### Author

Author is the person who originates, creates, and writes a `publication`.

___

#### Bid

A monetary offer (`offerPrice`) placed by a `bidder` during an `auction`.

A `Bid` contains:
- `bidder`: the `user` who placed the bid
- `offerPrice`: the amount of money offered in a single `bid` for the item
- `bidDate`: timestamp of when the bid was placed

A `bid` is immutable; once a bid is placed, it cannot be modified.

___

#### Bidder

A `user` who places one or more `bid`(s) in an `auction`(s).

___

#### Binding

Represents the way in which an `Edition` is fastened or held together (e.g., Hardcover binding).

___

#### Book

A `PublicationType` representing a `BOOK` as a physical item.

When the `PublicationType` is `BOOK` and was published during or after 1970, the `BOOK`'s `Edition` `Identifier` will be an `ISBN`. All `Edition`s, regardless of publication date, have a general `EditionId`. 

___

#### Buyer

A `Buyer` is a `User` who purchases `Item`(s) on the platform.

A `Buyer` can  provide a `Rating` to a `Seller` after a confirmed `Transaction` between both parts.

___

#### City

A `City` represents a geographical and administrative unit within a `Country`.

___

#### Community Member

Any `User` who participates in community-related activities.

`CommunityMembers` might engage in forum posts, blog posts or `BOOK` and `MAGAZINE` reviews.

___

#### Condition

Represents the physical condition of a `BOOK` or `MAGAZINE` (e.g., `GOOD`, `POOR`).

___

#### Country

A `Country` represents geographical country identified by its name.

---

#### Direct Sale

Represents a `DirectSale` of an `Item` with a specified `Price` and optional time limit.

___

#### Edition

An `Edition` models a specific released version of a `Publication` such as a `BOOK` or `MAGAZINE`, identified by its `Publication` metadata.

It includes both mandatory and optional fields.

Mandatory fields:
- `PublicationTypeId`: identifies the type of `Publication`(e.g., `BOOK`, `MAGAZINE`)
- `PublicationId`: identifies the catalog-level metadata capturing the immutable attributes of a `Publication`
- `Identifier`: may be an `ISBN` (for `BOOK`s published during or after 1970) or an `ISSN` (for `MAGAZINE`s)
- `PublishingCompanyId`: identifies the `PublishingCompany` that released the `Edition`
- `Year`: year in which the edition was published
- `Language`: language in which the publication is written
- `EditionId`: `Edition`'s automatically generated identity

Optional fields:
- `Dimension`: physical dimensions (height, width, and thickness) of a `BOOK` or `MAGAZINE`
- `Weight`: physical weight of a `BOOK` or `MAGAZINE`
- `NumberOfPages`: total number of pages of a `BOOK` or `MAGAZINE`
- `EditionNumber`: number identifying the `Edition` (e.g., first edition, second edition)
- `Binding`: the type of binding (e.g. hardcover, paperback)

___

#### Genre

Defines the style or category of a `Publication` (e.g., Fiction, Non-Fiction, Sci-Fi).

___

#### ISBN

International Standard Book Number: an `Identifier` for a `BOOK`, consisting of a unique numerical code assigned to each `BOOK` `Edition` published since 1970.
* There are two types of ISBN:
  * a 10-digit code for books published _since_ 1970 
  * a 13-digit code for books published _after_ 2007

---

#### ISSN

International Standard Serial Number: an `Identifier` for a `MAGAZINE`, consisting of a unique numerical code assigned to each `MAGAZINE` `Edition`.

---

#### Item

`Item` represents a physical or digital unit of a `Publication` available in the system.

It is identified by an `EditionId` and describes a specific instance of a `Publication`through its `Condition` and `Description`. It also maintains its current `SaleStatus` representing its lifecycle within the selling process.

An `Item` can transition between different sale states (e.g., not on sale, on auction, on direct sale, or sold), but it _cannot_ be simultaneously in multiple sale states.

___

#### Library

A `Library` is an entity that holds `Item`(s) (represented by `ItemId`s) owned by a `User`. 

Each `User` can only have one `Library`.

___

#### List of Items

A `ListOfItems` represents a list of `Items` created by a `User`.

A `User` can create several `ListOfItems`s. Each list has a `Name`, `Genre`, and associated `User`.

A `User` can only add to a `ListOfItems` `Items` that they have in their own `Library`.
By default, all lists are private, but they can be made public.

___

#### Logistic Service

An external entity responsible for handling shipment and delivery of sold items.

The `LogisticService` might: 

- generate a `ShippingLabel` for a `Transaction` after the `PaymentProcessing` is done
- collect a `Package` (of the `Item` prepared for shipping) from a `Seller` and deliver it to the `Buyer`

___

#### Magazine

A `PublicationType` representing a `MAGAZINE` as a physical item.

When the `PublicationType` is `MAGAZINE` its `Edition` `Identifier` will be an `ISSN`. It will also have a general `EditionId`.

___

#### Outright Price

A _buy now_ price that allows immediate purchase without waiting for an `Auction` to end.

___

#### Payment Processing

A process responsible for handling and confirming monetary exchanges between the `Buyer` and the `Seller` in a `Transaction`.

___

#### Phone Prefix

Represents a phone prefix of a given country/area.

___

#### Post

A `Post` is an essential part of the `SocialNetwork`. It is added by a registered `User`.

A `Post` must include a set of `Tags` and may include text, photos, videos, or references to specific `Publication`s shared in a public list.

A `Post` may be open or closed for comments.

___

#### Price

Represents a monetary price with a specific value and `Currency`.

___

#### Publication

Catalog-level metadata capturing the immutable attributes of a `Publication` independent of any physical copy.

`Publication` contains the following attributes:

- `PublicationId`: the `Publication`'s identity
- `Title`: the title of the `Publication`
- `AuthorId`: identity of the author or main contributor of a publication
- `Year`: the year the `Publication` was released
- `GenreId`: identity of the `Publication`'s genre

___

#### Publication Type

A classification that represents the type of publication (e.g. `BOOK`, `MAGAZINE`), used to classify and organize `Publication`s.

___

#### Publishing Company

Represents the organization or company that formally releases an `Edition`.

---

#### Sale Line

Represents a single purchase resulting from that `DirectSale` and it is created from a `ShoppingCartLine` during the checkout process.
A `SaleLine` belongs to exactly one `Sale`, while a `Sale` may contain one or more `SaleLine`s.

---

#### Sale

Represents the purchase of one or more items listed in `DirectSale`s. 
It is composed of one or more `SaleLine`s, each corresponding to a purchase.
It is created when a `Buyer` completes the checkout process from a `ShoppingCart`. 
It records the items purchased, their prices at the time of purchase, the participating `Seller`s, and the overall transaction amount.
A `Sale` may contain items from multiple `Seller`s but is always associated with a single `Buyer`.

---

#### Seller

A `Seller` is a `User` who sells an `Item`(s).

___

#### Shopping Cart

A `ShoppingCart` is a container owned by a `Buyer`, used for direct purchases **before checkout**.

A `ShoppingCart` is not binding. The `Buyer` might add or remove `Item`(s) at will before proceeding to a purchase (i.e, a `Transaction`).

One `shopping cart` may contain:

- one or more `ShoppingCartLine`s representing the `Item`(s) the `Buyer` intends to buy
- 
- `TotalAmount`: the total cost of all items currently in the `ShoppingCart`

___

#### Shopping Cart Line

Represents a single item selected for purchase from a `DirectSale`.

It may be transformed into a `SaleLine` when the `Buyer` completes the checkout process and a `Sale` is created.

A `ShoppingCartLine` belongs to exactly one `ShoppingCart`, while a `ShoppingCart` may contain one or more `ShoppingCartLine`s.

___

#### Social Network

The `SocialNetwork` is a platform component aiming to enable interaction between registered `Users` through content sharing and discussion.

Only `Author`s and `PublishingCompany` entities can register to post.

___

#### Support Service

A role played by a `User`, responsible for assisting `Buyer`s, `Seller`s and `Bidder`s and handling exceptional cases.

A `SupportService` may include:

- `CustomerSupport`: handles `User` inquiries and issues.
- `DisputeMediation`: manages conflicts between `Buyers` and `Sellers`
- `AppraisalService`: provides `Appraisal`s upon request. 

___

#### Title

Title of a `Publication`.

___

#### Transaction

A **completed** commercial exchange between a `Buyer` and a `Seller`.

A `Transaction` may contain a:

- `Date`: when the transaction was finalized
- `FinalPrice`: the final total `Price`
- `ShippingLabel`: composed of shipping documentation (e.g. `ShippingAddress`, `TrackingNumber`, etc.) used by a `LogisticService`
- `PaymentProcessing`: indicates that payment has been handled
- `AuctionTransactionLimit`: represents a constraint related to `Auction`-based `Transaction`s

___

#### User

Represents a registered entity (e.g., a person or a company) on the platform that may assume one or more roles.

A `User` may have the following attributes:

- `Name`: their full name
- `Address`: their physical `Address`
- `Email`: their contact `Email`
- `Phone`: their contact `Phone` number
- `Role`: the `Role` or set of `Role`s assigned to the `User`.

A `User` has an `ActivityHistory`, which records purchases and interactions performed on the platform, as well as a `Rating`, which represents a reputation score based on feedback from `Buyer`s and `Seller`s.

___

#### User Rating

Represents a `User`'s rating using a star-based system from one to five stars.

___

#### User Role

A classification that defines the permissions and responsibilities assigned to a `User` within the platform. A `User` may hold one or more roles simultaneously.

`Role`s may include:
- `USER`: the default `Role` assigned to every `User` during registration
- `ADMIN`: a `User` with elevated privileges

___

#### Weight

Represents the `Weight` of an object along with its unit of measurement (`WeightUnit`).

___