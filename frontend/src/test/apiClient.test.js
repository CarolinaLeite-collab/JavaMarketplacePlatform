import { describe, it, expect, vi, beforeEach } from 'vitest';
import { apiClient, BASE_URL, USER_ID } from '../services/apiClient';

const createResponse = (body, { status = 200 } = {}) => ({
    ok: status >= 200 && status < 300,
    status,
    text: vi.fn().mockResolvedValue(body),
    json: vi.fn().mockResolvedValue(
        body ? JSON.parse(body) : null
    )
});

// simulate a Vitest function
const mockFetch = vi.fn();
vi.stubGlobal('fetch', mockFetch);

// simulate a success status with data
const mockSuccess = (data) =>
    Promise.resolve({
        ok: true,
        status: 200,
        json: () => Promise.resolve(data)
    });

// simulate an error status
const mockError = (status) =>
    Promise.resolve({
        ok: false,
        status,
        text: () => Promise.resolve(String(status)),
        json: () => Promise.resolve({})
    });

// clean the history of mockFetch
beforeEach(() =>
    mockFetch.mockClear());


describe('apiClient', () => {

    describe('getListsOptions', () => {

        it('calls OPTIONS on my-lists endpoint', async () => {
            mockFetch.mockReturnValueOnce(
                Promise.resolve({
                    ok: true,
                    text: () =>
                        Promise.resolve(
                            JSON.stringify({ links: [] })
                        )
                })
            );

            await apiClient.getListsOptions();

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/my-lists?email=${USER_ID}`,
                {
                    method: 'OPTIONS',
                    headers: {
                        'X-User-Id': USER_ID
                    }
                }
            );
        });

        it('throws error when getListsOptions fails', async () => {
            mockFetch.mockReturnValueOnce(
                Promise.resolve({
                    ok: false,
                    status: 500
                })
            );

            await expect(
                apiClient.getListsOptions()
            ).rejects.toThrow('500');
        });
    });

    describe('getLibraryOptions', () => {
        it('calls OPTIONS on /my-library with user headers', async () => {
            mockFetch.mockResolvedValue(
                createResponse(
                    JSON.stringify({ _links: {} }),
                    { status: 200 }
                )
            );

            await apiClient.getLibraryOptions();

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/my-library?email=${USER_ID}`,
                {
                    method: 'OPTIONS',
                    headers: {
                        'X-User-Id': USER_ID
                    }
                }
            );
        });

        it('returns parsed options response', async () => {
            const data = {
                _links: {
                    collection: {
                        href: 'http://localhost:8081/my-library'
                    }
                }
            };

            mockFetch.mockResolvedValue(
                createResponse(
                    JSON.stringify(data),
                    { status: 200 }
                )
            );

            const result = await apiClient.getLibraryOptions();

            expect(result).toEqual(data);
        });

        it('throws when OPTIONS request fails', async () => {
            mockFetch.mockResolvedValue(
                createResponse('', { status: 500 })
            );

            await expect(
                apiClient.getLibraryOptions()
            ).rejects.toThrow('500');
        });
    });

    describe('getGenres', () => {

        it('calls the correct URL without X-User-Id', async () => {
            mockFetch.mockReturnValueOnce(mockSuccess({ genres: [] }));

            await apiClient.getGenres();

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/genres`
            );

            const callArgs = mockFetch.mock.calls[0];
            const headers = callArgs[1]?.headers ?? {};
            expect(headers['X-User-Id']).toBeUndefined();
        });


        it('returns data on success', async () => {
            const mockData = { genres: [{ genreId: 'ROMANCE', name: 'Romance' }] };
            mockFetch.mockReturnValueOnce(mockSuccess(mockData));

            const result = await apiClient.getGenres();

            expect(result).toEqual(mockData);
        });

    });

    describe('getByHref', () => {

        it('calls provided href with X-User-Id header', async () => {
            const href =
                `${BASE_URL}/my-library/ITEM-001`;

            mockFetch.mockReturnValueOnce(
                mockSuccess({})
            );

            await apiClient.getByHref(href);

            expect(mockFetch).toHaveBeenCalledWith(
                href,
                expect.objectContaining({
                    headers: {
                        'X-User-Id': USER_ID
                    }
                })
            );
        });

        it('returns data from href', async () => {
            const data = {
                publicationType: 'Book',
                authorName: 'George Orwell',
                identifier: '123456789'
            };

            mockFetch.mockReturnValueOnce(
                mockSuccess(data)
            );

            const result = await apiClient.getByHref(
                `${BASE_URL}/my-library/ITEM-001`
            );

            expect(result).toEqual(data);
        });

        it('throws error on failure', async () => {
            mockFetch.mockReturnValueOnce(
                mockError(404)
            );

            await expect(
                apiClient.getByHref('http://localhost:8081/test')
            ).rejects.toThrow('404');
        });

        it('returns null when getByHref returns 204', async () => {
            mockFetch.mockReturnValueOnce(
                Promise.resolve({
                    ok: true,
                    status: 204
                })
            );

            const result = await apiClient.getByHref(
                'http://localhost:8081/test'
            );

            expect(result).toBeNull();
        });

    });

    describe('createDirectSales', () => {

        it('sends POST request with body', async () => {
            mockFetch.mockReturnValueOnce(
                mockSuccess({ saleId: 'DS-001' })
            );

            const body = {
                libraryItemId: 'ITM-001',
                price: 12.50
            };

            await apiClient.createDirectSales(body);

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/direct-sales`,
                expect.objectContaining({
                    method: 'POST',
                    headers: expect.objectContaining({
                        'Content-Type': 'application/json',
                        'X-User-Id': USER_ID
                    }),
                    body: JSON.stringify(body)
                })
            );
        });

    });

    describe('postByHref', () => {
        it('calls POST on href with body', async () => {
            const href =
                'http://localhost:8081/my-lists';

            const body = {
                name: 'Favorites'
            };

            mockFetch.mockReturnValueOnce(
                mockSuccess({})
            );

            await apiClient.postByHref(href, body);

            expect(mockFetch).toHaveBeenCalledWith(
                href,
                expect.objectContaining({
                    method: 'POST',
                    headers: expect.objectContaining({
                        'Content-Type': 'application/json',
                        'X-User-Id': USER_ID
                    }),
                    body: JSON.stringify(body)
                })
            );
        });

        it('throws error when postByHref fails', async () => {
            mockFetch.mockReturnValueOnce(
                Promise.resolve({
                    ok: false,
                    text: () => Promise.resolve('Invalid request')
                })
            );

            await expect(
                apiClient.postByHref(
                    'http://localhost:8081/test',
                    {}
                )
            ).rejects.toThrow('Invalid request');
        });
    });

    describe('patchByHref', () => {
        it('calls PATCH with body', async () => {
            const href =
                'http://localhost:8081/my-lists/1';

            const body = {
                name: 'Updated'
            };

            mockFetch.mockReturnValueOnce(
                mockSuccess({})
            );

            await apiClient.patchByHref(href, body);

            expect(mockFetch).toHaveBeenCalledWith(
                href,
                expect.objectContaining({
                    method: 'PATCH',
                    body: JSON.stringify(body)
                })
            );
        });

        it('throws error when patchByHref fails', async () => {
            mockFetch.mockReturnValueOnce(
                mockError(400)
            );

            await expect(
                apiClient.patchByHref(
                    'http://localhost:8081/test',
                    {}
                )
            ).rejects.toThrow('400');
        });
    });

    describe('patchNoBodyByHref', () => {
        it('calls PATCH without body', async () => {
            const href =
                'http://localhost:8081/my-lists/share';

            mockFetch.mockReturnValueOnce(
                mockSuccess({})
            );

            await apiClient.patchNoBodyByHref(href);

            expect(mockFetch).toHaveBeenCalledWith(
                href,
                expect.objectContaining({
                    method: 'PATCH',
                    headers: {
                        'X-User-Id': USER_ID
                    }
                })
            );
        });

        it('throws error when patchNoBodyByHref fails', async () => {
            mockFetch.mockReturnValueOnce(
                mockError(404)
            );

            await expect(
                apiClient.patchNoBodyByHref(
                    'http://localhost:8081/test'
                )
            ).rejects.toThrow('404');
        });
    });

    describe('deleteByHref', () => {
        it('calls DELETE endpoint', async () => {
            const href =
                'http://localhost:8081/my-lists/1';

            mockFetch.mockReturnValueOnce(
                Promise.resolve({
                    ok: true,
                    status: 204
                })
            );

            const result =
                await apiClient.deleteByHref(href);

            expect(result).toBeNull();

            expect(mockFetch).toHaveBeenCalledWith(
                href,
                expect.objectContaining({
                    method: 'DELETE',
                    headers: {
                        'X-User-Id': USER_ID
                    }
                })
            );
        });

        it('throws error when deleteByHref fails', async () => {
            mockFetch.mockReturnValueOnce(
                mockError(404)
            );

            await expect(
                apiClient.deleteByHref(
                    'http://localhost:8081/test'
                )
            ).rejects.toThrow('404');
        });
    });

    describe('getRootOptions', () => {
        it('fetches root options successfully', async () => {
            const response = {
                _links: {
                    genres: {
                        href: 'http://localhost:8081/genres'
                    }
                }
            };

            mockFetch.mockReturnValueOnce(
                Promise.resolve({
                    ok: true,
                    text: () => Promise.resolve(JSON.stringify(response))
                })
            );

            const result = await apiClient.getRootOptions();

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/api?email=${USER_ID}`,
                {
                    method: 'OPTIONS',
                    headers: {
                        'X-User-Id': USER_ID
                    }
                }
            );

            expect(result).toEqual(response);
        });
    });

    describe('getDirectSales', () => {
        it('fetches direct sales successfully', async () => {
            const response = [{ saleId: 'SALE-001' }];

            mockFetch.mockReturnValueOnce(
                Promise.resolve({
                    ok: true,
                    status: 200,
                    json: () => Promise.resolve(response)
                })
            );

            const result = await apiClient.getDirectSales();

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/direct-sales`
            );

            expect(result).toEqual(response);
        });
    });

    describe('getItemById', () => {
        it('fetches item by id successfully', async () => {
            const response = {
                itemId: 'ITEM-001',
                title: 'Dune'
            };

            mockFetch.mockReturnValueOnce(
                Promise.resolve({
                    ok: true,
                    status: 200,
                    json: () => Promise.resolve(response)
                })
            );

            const result = await apiClient.getItemById('ITEM-001');

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/items/ITEM-001`
            );

            expect(result).toEqual(response);
        });
    });



});