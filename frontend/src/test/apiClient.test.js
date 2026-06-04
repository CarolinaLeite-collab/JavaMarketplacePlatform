import { describe, it, expect, vi, beforeEach } from 'vitest';
import { apiClient, BASE_URL, USER_ID } from '../services/apiClient';


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

    describe('getLibrary', () => {

        it('calls endpoint with X-User-Id header', async () => {
            mockFetch.mockReturnValueOnce(
                mockSuccess({ items: [] })
            );

            await apiClient.getLibrary();

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/my-library/`,
                expect.objectContaining({
                    headers: {
                        'X-User-Id': USER_ID
                    }
                })
            );
        });

        it('returns library data', async () => {
            const data = {
                items: []
            };

            mockFetch.mockReturnValueOnce(
                mockSuccess(data)
            );

            const result = await apiClient.getLibrary();

            expect(result).toEqual(data);
        });

        it('throws error on failure', async () => {
            mockFetch.mockReturnValueOnce(mockError(404));

            await expect(
                apiClient.getByHref`${BASE_URL}/my-library/INVALID`
            ).rejects.toThrow('404');
        });

        it('returns null when library endpoint returns 204', async () => {
            mockFetch.mockReturnValueOnce(
                Promise.resolve({
                    ok: true,
                    status: 204
                })
            );

            const result = await apiClient.getLibrary();

            expect(result).toBeNull();
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

            await expect(apiClient.getLibrary()).rejects.toThrow('404');
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

});