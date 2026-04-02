# MiteLovers - Domain Model Glossary

___

## Table of contents

- [Activity History](#activity-history)
- [Appraisal Entity](#appraisal-entity)
- [Auction](#auction)
- [Bid](#bid)
- [Bidder](#bidder)
- [Book](#book)
- [Buyer](#buyer)
- [Community Member](#community-member)
- [Item](#item)
- [Listing](#listing)
- [Logistic Service](#logistic-service)
- [Magazine](#magazine)
- [Outright Price](#outright-price)
- [Payment Processing](#payment-processing)
- [Publication Info](#publication-info)
- [Publishing Company](#publishing-company)
- [Publication Type](#publication-type)
- [Seller](#seller)
- [Shopping Cart](#shopping-cart)
- [Support Service](#support-service)
- [Transaction](#transaction)
- [User](#user)
- [User Rating](#user-rating)
- [Title](#title)
- [Weight](#weight)

___

#### Activity History

The `Activity History` tracks and saves a `user`'s past actions or transactions (buying or selling) within the marketplace.

___

#### Appraisal Entity

An `appraisal entity` is a registered `user` that is responsible for evaluating the condition, authenticity, or value of an item before listing or sale.

___

#### Appraisal Service

A `support service` role responsible for coordinating item appraisals.

___

#### Auction

A selling mechanism where a listing is sold through competitive bidding within a defined time window. 

`Bids` determine the final sale price. 

The `seller` defines the following attributes for an `auction`:

- a `minPrice`, which is the starting price for the auction; 
- a `reservePrice`, which is the minimum acceptable price for the seller. If not reached, the item may not be sold and might be relisted;
- an `outrightPrice` which is a price set in advance by the seller that allows
  a ‘buyer to purchase the item immediately, without waiting for the auction
  to end and without any further bidding;
- an `auctionStartDate`, which is the date and time when the auction becomes active;
- an `auctionEndDate`, the date and time when the auction closes.

Items **cannot** be simultaneously on direct sale (via `shoppingCart`) and on `auction`.

___

#### Bid

A `bid` is a monetary offer (`offerPrice`) placed by a `bidder` during an `auction`.

A `Bid` contains:

- the `offerPrice`, which is the amount of money offered in a single `bid` for the item.

___

#### Bidder

A role played by a `user` who can place `bids` in `auctions`.

___

#### Book

A type of publication representing a book as a physical item.

The `book` can be defined by:

- `publicationInfo` describes it's general attributes;
- `condition` represents the physical state of the book (e.g. new, good, poor).

___

#### Buyer

A role played by a `user` who purchases items on the platform.

A `buyer` has an `activityHistory`, which is the record of purchases and interactions made by the buyer.

A `buyer` can also provide a `rating` to a `seller` after a confirmed `transaction` between both parts.

___

#### Community Member

A role played by a `user`, participating in community-related activities.

`Community members` might engage in forum posts, blog posts or `book` and `magazine` reviews.

___

#### Item

`Item` is an abstract placeholder representing a single sellable physical object, ensuring a listing contains exactly one book or one magazine.

___

#### Listing

An offer created by a `seller` to sell an `item`, either via `auction` or direct sale (via `shoppingCart`).

The `listing` can have:

- `date`, which is the date when the listing was created.
- `price`, which is the fixed price used for direct sales (via `shoppingCart`).
- `auction`, in which the winning `bid` will determine the final sale price.

___

#### Logistic Service

An external entity responsible for handling shipment and delivery of sold items.

The `logistic service` might: 

- generate a `shipping label` for a `transaction` after the `paymentProcessing` is done.
- It will collect a `package` (the physical `item` prepared for shipping) from a `seller`and deliver it to the `buyer`.

___

#### Magazine

A type of publication representing a magazine as a physical item.

The `magazine` can be defined by:

- `publicationInfo` describes it's general attributes;
- `condition` represents the physical state of the magazine (e.g. new, good, poor).

___

#### Outright Price

The `outright price` is a `price` set in advance by the `seller` that allows a `buyer` to purchase the `item` immediately, without waiting for the `auction` to end and without any further bidding.

___

#### Payment Processing

A process responsible for handling and confirming monetary exchanges between the `buyer` and the `seller` in `transactions`.

___

#### Publication Info

Catalog-level metadata describing a publication independently of any physical copy.

`PublicationInfo` might contain the following attributes:

- `title` is the title of the publication.
- `publicationDate` is the date the publication was released.
- `genre` corresponds to the literary or editorial genre.
- `binding` is the type of binding (e.g. hardcover, paperback).
- `ISBN` stands for *International Standard Book Number* (for books).
- `ISSN` stands for *International Standard Serial Number* (for magazines).
- `author` is the author or main contributor of a `book`.
- `contributors` is the list of individuals who contributed to a `magazine`.
- `language` is the language in which the publication is written.
- `editor` is the person or entity responsible for the editorial content.
- `publisher` is the publishing house (i.e. the label) that released the publication.
- `edition` corresponds to a specific printing or release (e.g. 1st edition, 2nd edition).
- `numberOfPages` is the total number of pages of a `book` or `magazine`.
- `dimensions3D` are the physical dimensions (height, width, and thickness) of a `book` or `magazine`.
- `weight` is the physical weight of a `book` or `magazine`.
- `description` is a short text describing the contents of a `book` or `magazine`.

___

#### Publication Type

A classification that defines the category of a publication (e.g. book, magazine) and allows the system to classify and organize publications.

___

#### Publishing Company

Represents the organization or company that formally releases the work (`publication`).

___

#### User Role

A classification that defines the permissions and responsibilities assigned to a user within the platform. A user may hold one or more roles simultaneously.

`Roles` may include:
- `USER`, the default role assigned to every user during registration.
- `ADMIN`, a user with elevated privileges.

___

#### Seller

A role played by a `user` who creates `listings` to sell `items`.

A `seller` may have :

- `listings`, each with one `item`;
- `activityHistory`, which is the record of all `listings` and `transactions` performed by the seller;
- `rating`, which corresponds to a reputation score based on `buyer` feedback.

___

#### Shopping Cart

A `shopping cart` is a container owned by a `buyer`, used for direct purchases **before checkout**.

A shopping cart is not binding. The `buyer` might add or remove `listings` at will before proceeding to a purchase (i.e, a `transaction`).

One `shopping cart` will contain:

- one, or several `listings`, that represent all the `items` a `buyer` intends to buy;
- totalPrice – The total cost of all listings currently in the cart.

___

#### Support Service

A role played by a `user`, responsible for assisting `buyers`, `sellers` or `bidders` and handling exceptional cases.

A `support service` may include:

- `customerSupport`, that handles user inquiries and issues.
- `disputeMediation`, that manages conflicts between buyers and sellers.
- `appraisalService`, that provides appraisals upon request. 

___

#### Transaction

A **completed** commercial exchange between `buyer` and `seller`.

A `transaction` may contain a:

- `date` – Date the transaction was finalized.
- `finalPrice` is the final total price.
- `shippingLabel`, is composed of shipping documentation (e.g. shipping adress, tracking number, etc.) used by a `logistic service`.
- `paymentProcessing`, indicates that payment has been handled.
- `auctionTransactionLimit` represents a constraint related to auction-based transactions.

___

#### Title

Title of a publication.

___

#### User

A registered entity (may be a person, company, or even an AI agent) on the platform who may play one or more roles.

Users may have attributes such as:

- `name`, full name of the user.
- `address`, physical address.
- `email`, contact email.
- `phoneNumber`, contact phone number.

___

#### User Rating

Represents a user's rating using a star-based system from one to five stars.

___

#### Weight

Represents the weight of an object along with an unit of measurement (`WeightUnit`).

___