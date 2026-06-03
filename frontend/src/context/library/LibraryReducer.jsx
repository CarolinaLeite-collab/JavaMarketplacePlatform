import {
    LOADING,
    FETCH_LIBRARY_SUCCESS,
    FETCH_LIBRARY_ERROR,
    FETCH_DETAIL_SUCCESS,
    FETCH_DETAIL_ERROR
} from './LibraryActions';

export const initialState = {
    items: [],
    details: {},
    loading: false,
    error: null
};

function mapItem(item) {
    const links = item._links
        ? Object.entries(item._links).map(([rel, val]) => ({ rel, href: val.href }))
        : [];

    return {
        itemId: item.itemId,
        title: item.title,
        picture: item.picture ?? null,
        links: links
    };
}

function formatPublicationType(type) {
    if (!type) return '';
    return type
        .toLowerCase()
        .replace(/\b\w/g, c => c.toUpperCase());
}

export function libraryReducer(state, action) {
    switch (action.type) {

        case LOADING:
            return { ...state, loading: true, error: null };

        case FETCH_LIBRARY_SUCCESS:
            return { ...state, loading: false, items: action.payload.map(mapItem) };

        case FETCH_LIBRARY_ERROR:
            return { ...state, loading: false, error: action.payload };

        case FETCH_DETAIL_SUCCESS:
            return {
                ...state,
                details: {
                    ...state.details,
                    [action.payload.itemId]: {
                        ...action.payload.detail,
                        publicationType: formatPublicationType(action.payload.detail.publicationType)
                    }
                }
            };

        case FETCH_DETAIL_ERROR:
            return { ...state, error: action.payload };

        default:
            return state;
    }
}