
const BASE_URL = 'http://localhost:8081';
const USER_ID = 'angelo@aeiou.com';       // temporary until having authorization

// GET generic (without authorization)
async function getPublic(path) {
    const response = await fetch(`${BASE_URL}${path}`);

    if (!response.ok) {
        throw new Error(`${response.status}`);
    }
    if (response.status === 204) {
        return null;
    }

    return response.json();
}

// GET authentication (with X-User-Id)
async function getPrivate(path) {
    const response = await fetch(`${BASE_URL}${path}`, {
        headers: {
            'X-User-Id': USER_ID
        }
    });

    if (!response.ok) {
        throw new Error(`${response.status}`);
    }
    if (response.status === 204) {
        return null;
    }

    return response.json();
}

async function post(path, body) {
    const response = await fetch(`${BASE_URL}${path}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-User-Id': USER_ID
        },
        body: JSON.stringify(body)
    });

    if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(errorMessage);
    }
    if (response.status === 204) {
        return null;
    }

    return response.json();
}

async function patch(path, body) {
    const response = await fetch(`${BASE_URL}${path}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            'X-User-Id': USER_ID
        },
        body: JSON.stringify(body)
    });

    if (!response.ok) {
        throw new Error(`${response.status}`);
    }
    if (response.status === 204) {
        return null;
    }

    return response.json();
}

async function patchNoBody(path) {
    const response = await fetch(`${BASE_URL}${path}`, {
        method: 'PATCH',
        headers: {
            'X-User-Id': USER_ID
        }
    });

    if (!response.ok) {
        throw new Error(`${response.status}`);
    }
    if (response.status === 204) {
        return null;
    }

    return response.json();
}

async function deleteReq(path) {
    const response = await fetch(`${BASE_URL}${path}`, {
        method: 'DELETE',
        headers: {
            'X-User-Id': USER_ID
        }
    });

    if (!response.ok) {
        throw new Error(`${response.status}`);
    }

    return null;
}

async function deleteByHref(href) {
    const path = new URL(href).pathname;
    return deleteReq(path);
}

async function patchByHref(href, body) {
    const path = new URL(href).pathname;
    return patch(path, body);
}

async function patchNoBodyByHref(href) {
    const path = new URL(href).pathname;
    return patchNoBody(path);
}

// Contract endpoints
export const apiClient = {
    getGenres: () =>
        getPublic('/genres'),

    getMyLists: () =>
        getPrivate('/my-lists/'),

    getLibraryItem: (href) =>
        getPrivate(href),

    getLibrary: () =>
        getPrivate('/my-library'),

    createList: (body) =>
        post('/my-lists/', body),

    createDirectSales: (body) =>
        post('/direct-sales', body),

    makeListPublic: (listId, body) =>
        patch(`/my-lists/${listId}/visibility`, body),

    makeListPrivate: (listId) =>
        patchNoBody(`/my-lists/${listId}/visibility`),

    addItemToList: (listId, body) =>
        post(`/my-lists/${listId}`, body),

    deleteList: (listId) =>
        deleteReq(`/my-lists/${listId}`),

    deleteByHref: (href) =>
        deleteByHref(href),

    patchByHref: (href, body) =>
        patchByHref(href, body),

    patchNoBodyByHref: (href) =>
        patchNoBodyByHref(href),
};