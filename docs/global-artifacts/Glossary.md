# MiteLovers - Domain Model Glossary

___
## Table of contents

- [Activity History](#activity-history)
- [Appraisal Entity](#appraisal-entity)
- [Auction](#auction)
- [Author](#author)
- [Bid](#bid)
- [Bidder](#bidder)
- [Book](#book)
- [Buyer](#buyer)
- [City](#city)
- [Country](#country)
- [Community Member](#community-member)
- [Direct Sale](#direct-sale)
- [Edition](#edition)
- [Genre](#genre)
- [Item](#item)
- [Library](#library)
- [List of Items](#list-of-items)
- [Listing](#listing)
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
- [Transaction](#transaction)
- [Title](#title)
- [User](#user)
- [User Rating](#user-rating)
- [User Role](#user-role)
- [Weight](#weight)

___

#### Activity History

The `Activity History` tracks and saves a `user`'s past actions or transactions (buying or selling) within the marketplace.

___

#### Appraisal Entity

An `appraisal entity` is a registered `user` that is responsible for evaluating the condition, authenticity, or value of an `item` before it is put up for sale.
Contains the appraisal entity's `name`, the list of `publication type Id's` it can appraise, and the list of `genre Id's` it specializes in.

___

#### Auction

 Represents a time-bounded selling mechanism where one or more Items is sold via competitive bidding. 
 An auction is active only within its configured time window: `auctionStartDate` to `auctionEndDate`.
 `Bids` placed during the active period determine the `final sale price` and winning `buyer`.

The `seller` defines the following attributes for an `auction`:

- a `startingPrice`, which is the minimum price required for the first valid bid.
- a `reservePrice`, which is the minimum acceptable price for the item to be sold; bids below this price do not result in a sale.
- an `outrightPrice`, which is a `buy now` price that allows immediate purchase without waiting for the auction to end.
- an `auctionStartDate`, which is the date and time when the auction becomes active.
- an `auctionEndDate`, which is the date and time when the auction closes.

Items **cannot** be simultaneously on direct sale (via `shoppingCart`) and on `auction`.

___

#### Author

Author is the person who originates, creates, and writes a `publication`.

___

#### Bid

A `bid` is a monetary offer (`offerPrice`) placed by a `bidder` during an `auction`.

A `Bid` contains:
- the `bidder`, which is the `user` who placed the bid.
- the `offerPrice`, which is the amount of money offered in a single `bid` for the item.
- the `bidDate`, which is the timestamp when the bid was placed

A `bid` is immutable - once a bid is placed, it cannot be modified.

___

#### Bidder

A `user` who places `bids` in `auctions`.

___

#### Book

Is a `publication type` representing a book as a physical item.

When the publication type is `BOOK`, an `Edition identifier` must be an `ISBN`.

___

#### Buyer

A `buyer` is a `user` who purchases items on the platform.

A `buyer` can  provide a `rating` to a `seller` after a confirmed `transaction` between both parts.

___

#### City

A `city` represents a geographical and administrative unit within a `country`.

___

#### Country

A `country` represents geographical country identified by its name.

___

#### Community Member

Any `user` who participates in community-related activities.

`Community members` might engage in forum posts, blog posts or `book` and `magazine` reviews.

___

#### Direct Sale

Represents a `direct sale` of an `item` with a specified `price` and optional time limit.

___

#### Edition

An `edition` models a specific released version of a publication such as a book or magazine, identified by its publication metadata.

It includes both mandatory and optional fields.

Mandatory fields:
- `publication`, that is the catalog-level metadata capturing the immutable attributes of a publication.
- `identifier`, that can be `ISBN` (for books) or `ISSN` (for magazines).
- `publishing company` is the publishing house (i.e. the label) that released the publication.
- `publishing year`, that is the year in which the edition was published.
- `language` is the language in which the publication is written. 

Optional fields:
- `dimensions` are the physical dimensions (height, width, and thickness) of a `book` or `magazine`.
- `weight` is the physical weight of a `book` or `magazine`.
- `numberOfPages` is the total number of pages of a `book` or `magazine`.
- `edition number`, the number identifying the edition (e.g., first edition, second edition).
- `binding` is the type of binding (e.g. hardcover, paperback).

___

#### Genre

Represents a `genre` in the MiteLovers domain.

___

#### Library

A `library` is an entity that groups publications and is owned by a `user`. 

Each `user` can only have one `library`.

___

#### Item

`Item` is an abstract placeholder representing a single sellable physical object, ensuring a listing contains exactly one book or one magazine.

___

#### List of Items

A `list of items` represents a list of `items` created by a `user`.

A `user` can create several lists. Each list has a `name`, a `genre`.
A `user` can only add to a list items that they have in their own `library`.
By default, all lists are `private`, but they can be made `public`.

___

#### Logistic Service

An external entity responsible for handling shipment and delivery of sold items.

The `logistic service` might: 

- generate a `shipping label` for a `transaction` after the `paymentProcessing` is done.
- It will collect a `package` (the physical `item` prepared for shipping) from a `seller`and deliver it to the `buyer`.

___

#### Magazine

Is a `publication type` representing a magazine as a physical item.

When the publication type is `MAGAZINE`, an `Edition identifier` must be an `ISSN`.

___

#### Outright Price

The `outright price` is a `buy now` price that allows immediate purchase without waiting for the `auction` to end.

___

#### Payment Processing

A process responsible for handling and confirming monetary exchanges between the `buyer` and the `seller` in `transactions`.

___

#### Phone Prefix

Represents a phone prefix of a given country.

___

#### Post

A `post` is an essential part of the `social network`. It is added by a registered `user`.
A `post` must include a set of `tags` and may include text, photos, videos, or references to specific `publications` shared in a `public list`. 
A `post` may be open or closed for comments.

___

#### Price

Represents a monetary price with a specific `value` and a `currency`.

___

#### Publication

Catalog-level metadata capturing the immutable attributes of a publication independently of any physical copy.

`Publication` contains the following attributes:

- `title` is the title of the publication.
- `author` is the author or main contributor of a publication.
- `release year` is the year the publication was released.
- `genre` corresponds to the literary or editorial genre.

___

#### Publication Type

A classification that represents the type of publication (e.g. BOOK, MAGAZINE), used to classify and organize `publications`.

___

#### Publishing Company

Represents the organization or company that formally releases one `edition`.

___

#### User Role

A classification that defines the permissions and responsibilities assigned to a user within the platform. A user may hold one or more roles simultaneously.

`Roles` may include:
- `USER`, the default role assigned to every user during registration.
- `ADMIN`, a user with elevated privileges.

___

#### Seller

A `seller` is a `user` who sells `items`.

___

#### Shopping Cart

A `shopping cart` is a container owned by a `buyer`, used for direct purchases **before checkout**.

A shopping cart is not binding. The `buyer` might add or remove `listings` at will before proceeding to a purchase (i.e, a `transaction`).

One `shopping cart` will contain:

- one, or several `listings`, that represent all the `items` a `buyer` intends to buy;
- totalPrice – The total cost of all listings currently in the cart.

___

#### Social Network

The `social network` is a platform component aiming to enable interaction between registered `users` through content sharing and discussion.
Only `authors`and `publishing companies`can register to post.

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

Represents a registered entity (e.g., a person or a company) on the platform that may assume one or more roles.

A user may have the following attributes:

- `name`, the full name of the user.
- `address`, the physical address.
- `email`, the contact email.
- `phone`, the contact phone number.
- `role`, the role or set of roles assigned to the user.

A `user` has an `activityHistory`, which records purchases and interactions performed on the platform, and a `rating`, which represents a reputation score based on feedback from `buyers` and `sellers`.

___

#### User Rating

Represents a user's rating using a star-based system from one to five stars.

___

#### Weight

Represents the weight of an object along with an unit of measurement (`WeightUnit`).

___