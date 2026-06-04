export const BASE_URL = 'http://localhost:8081';
export let USER_ID = 'pedro@aeiou.com'; // temporary until having authorization

export function setUserId(id) {
    console.log('switching user to:', id);
    USER_ID = id;
}

// Entry point functions (hardcoded paths - initial discovery)
async function optionsByPath(path) {
    console.log('optionsByPath - current USER_ID:', USER_ID);
    const response = await fetch(`${BASE_URL}${path}?email=${USER_ID}`, {
        method: 'OPTIONS',
        headers: { 'X-User-Id': USER_ID }
    });
    if (!response.ok) throw new Error(`${response.status}`);
    const text = await response.text();
    return JSON.parse(text);
}

async function getPublic(path) {
    const response = await fetch(`${BASE_URL}${path}`);
    if (!response.ok) throw new Error(`${response.status}`);
    if (response.status === 204) return null;
    return response.json();
}

async function getPrivate(path) {
    const response = await fetch(`${BASE_URL}${path}`, {
        headers: { 'X-User-Id': USER_ID }
    });
    if (!response.ok) throw new Error(`${response.status}`);
    if (response.status === 204) return null;
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
    if (response.status === 204) return null;
    return response.json();
}

// HATEOAS functions (use full href from backend response links)
async function getByHref(href) {
    console.log('getByHref:', href, 'USER_ID:', USER_ID);
    const response = await fetch(href, {
        headers: { 'X-User-Id': USER_ID }
    });
    if (!response.ok) throw new Error(`${response.status}`);
    if (response.status === 204) return null;
    return response.json();
}

async function postByHref(href, body) {
    const response = await fetch(href, {
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
    if (response.status === 204) return null;
    return response.json();
}

async function patchByHref(href, body) {
    const response = await fetch(href, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            'X-User-Id': USER_ID
        },
        body: JSON.stringify(body)
    });
    if (!response.ok) throw new Error(`${response.status}`);
    if (response.status === 204) return null;
    return response.json();
}

async function patchNoBodyByHref(href) {
    const response = await fetch(href, {
        method: 'PATCH',
        headers: { 'X-User-Id': USER_ID }
    });
    if (!response.ok) throw new Error(`${response.status}`);
    if (response.status === 204) return null;
    return response.json();
}

async function deleteByHref(href) {
    const response = await fetch(href, {
        method: 'DELETE',
        headers: { 'X-User-Id': USER_ID }
    });
    if (!response.ok) throw new Error(`${response.status}`);
    return null;
}

// Contract endpoints
export const apiClient = {
    // Entry points — hardcoded
    getRootOptions: () => optionsByPath('/api'),
    getListsOptions: () => optionsByPath('/my-lists'),
    getGenres: () => getPublic('/genres'),
<<<<<<< Updated upstream
    getLibrary: () => getPrivate('/my-library/'),
    getMyLibraryItemsForSale: () => getPrivate('/items/my-library'),
=======
    getLibraryOptions: () => optionsByPath('/my-library'),
>>>>>>> Stashed changes
    createDirectSales: (body) => post('/direct-sales', body),
    getDirectSales: () => getPublic('/direct-sales'),
    getItemById: (itemId) => getPublic(`/items/${itemId}`),

    // HATEOAS — use full href from backend response links
    getByHref: (href) => getByHref(href),
    postByHref: (href, body) => postByHref(href, body),
    patchByHref: (href, body) => patchByHref(href, body),
    patchNoBodyByHref: (href) => patchNoBodyByHref(href),
    deleteByHref: (href) => deleteByHref(href),
};