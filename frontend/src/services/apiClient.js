
const BASE_URL = 'http://localhost:8080';
const USER_ID = 'pedro@mail.com';       // temporary until having authorization

// GET generic (without authorization)
async function getPublic(path) {
    const response = await fetch(`${BASE_URL}${path}`);

    if (!response.ok) {
        throw new Error(`${response.status}`);
    }

    return response.json();
}

// GET autentication (with X-User-Id)
async function getPrivate(path) {
    const response = await fetch(`${BASE_URL}${path}`, {
        headers: {
            'X-User-Id': USER_ID
        }
    });

    if (!response.ok) {
        throw new Error(`${response.status}`);
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
        throw new Error(`${response.status}`);
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

    return response.json();
}

// Contract endpoints
export const apiClient = {
    getMyLists: () =>
        getPrivate('/my-lists/'),

    getGenres: () =>
        getPublic('/genres'),

    createList: (body) =>
        post('/my-lists/', body),

    shareList: (href, body) =>
        patch(href, body),

    getLibrary: () =>
        getPrivate('/my-library'),

    getLibraryItem: (href) =>
        getPrivate(href),

};