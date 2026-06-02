export type LibraryAction =
| { type: 'FETCH_LIBRARY_SUCCESS'; payload: LibraryItem[] }
| { type: 'FETCH_LIBRARY_ERROR'; payload: string }
| { type: 'FETCH_DETAIL_SUCCESS'; payload: { itemId: string; detail: LibraryItemDetail } }
| { type: 'FETCH_DETAIL_ERROR'; payload: string }
| { type: 'LOADING' };

export interface LibraryItem {
    itemId: string;
    title: string;
    picture: string | null;
    _links: { self: { href: string } };
}

export interface LibraryItemDetail {
    authorName: string;
    identifier: string | null;
    publicationType: string;
}