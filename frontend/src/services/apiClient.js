
const BASE_URL = 'http://localhost:8081';
const USER_ID = 'pedro@mail.com';       // temporary until having authorization

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
        throw new Error(`${response.status}`);
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

    shareList: (href, body) =>
        patch(href, body)

};