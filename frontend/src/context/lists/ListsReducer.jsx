import {
    GET_LIST_OPTIONS_SUCCESS, GET_LIST_OPTIONS_ERROR,
    CREATE_LIST_SUCCESS, CREATE_LIST_ERROR,
    GET_LISTS_SUCCESS, GET_LISTS_ERROR,
    GET_GENRES_SUCCESS, GET_GENRES_ERROR,
    MAKE_LIST_PUBLIC_SUCCESS, MAKE_LIST_PUBLIC_ERROR,
    MAKE_LIST_PRIVATE_SUCCESS, MAKE_LIST_PRIVATE_ERROR,
    DELETE_LIST_SUCCESS, DELETE_LIST_ERROR,
    ADD_ITEM_TO_LIST_SUCCESS, ADD_ITEM_TO_LIST_ERROR,
} from './ListsActions';

export const initialListsState = {
    lists: [],
    genres: [],
    error: null,
};

function mapList(item) {
    const links = item._links
        ? Object.entries(item._links).map(([rel, val]) => ({ rel, href: val.href }))
        : [];

    return {
        listId: item.listId,
        name: item.name,
        genre: formatGenre(item.genreId),
        visibility: item.private ? 'private' : 'public',
        sharedUntil: item.sharedUntil ? daysLeft(item.sharedUntil) : null,
        links: links,
        itemIds: item.itemsId ?? [],
    };
}

function getLink(links, rel) {
    return links?.find(l => l.rel === rel)?.href || null;
}

function formatGenre(genreId) {
    return genreId
        .replace(/-/g, ' ')
        .toLowerCase()
        .replace(/\b\w/g, (char) => char.toUpperCase());
}

function daysLeft(dateString) {
    const now = new Date();
    const until = new Date(dateString);
    const diff = Math.ceil((until - now) / (1000 * 60 * 60 * 24));
    return diff > 0 ? diff : null;
}

export function listsReducer(state, action) {
    switch (action.type) {
        case GET_LIST_OPTIONS_SUCCESS:
            return {
                ...state,
                createListHref: action.payload?.['create-list']?.href ?? null,
                myListsHref: action.payload?.['collection']?.href ?? null,
            };
        case GET_LIST_OPTIONS_ERROR:
            return { ...state, error: action.payload };
        case GET_LISTS_SUCCESS: {
            const payload = action.payload;
            if (!payload) {
                return { ...state, error: null, lists: [] };
            }
            const lists = Array.isArray(payload)
                ? payload
                : payload?._embedded?.listOfItemsResponseDTOList ?? [];
            return { ...state, error: null, lists: lists.map(mapList) };
        }
        case GET_LISTS_ERROR:
            return { ...state, error: action.payload, loading: false };
        case CREATE_LIST_SUCCESS:
            return { ...state, error: null, lists: [...state.lists, mapList(action.payload)] };
        case CREATE_LIST_ERROR:
            return { ...state, error: action.payload };
        case GET_GENRES_SUCCESS:
            return {
                ...state,
                error: null,
                genres: action.payload.map((g) => ({
                    value: g.genreId,
                    label: g.genreName
                }))
            };
        case GET_GENRES_ERROR:
            return { ...state, error: action.payload };
        case MAKE_LIST_PUBLIC_SUCCESS:
        case MAKE_LIST_PRIVATE_SUCCESS:
            return {
                ...state,
                error: null,
                lists: state.lists.map((list) =>
                    list.listId === action.payload.listId
                        ? mapList(action.payload)
                        : list
                )
            };
        case MAKE_LIST_PUBLIC_ERROR:
        case MAKE_LIST_PRIVATE_ERROR:
            return { ...state, error: action.payload };
        case DELETE_LIST_SUCCESS:
            return {
                ...state,
                error: null,
                lists: state.lists.filter((list) => list.listId !== action.payload)
            };
        case DELETE_LIST_ERROR:
            return { ...state, error: action.payload };
        case ADD_ITEM_TO_LIST_SUCCESS:
            return {
                ...state,
                error: null,
                lists: state.lists.map((list) =>
                    list.listId === action.payload.listId
                        ? mapList(action.payload)
                        : list
                )
            };
        case ADD_ITEM_TO_LIST_ERROR:
            return { ...state, error: action.payload };
        default:
            return state;
    }
}