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

    describe('getGenres', () => {

        it('calls the correct URL without X-User-Id', async () => {
            mockFetch.mockReturnValueOnce(mockSuccess({ genres: [] }));

            await apiClient.getGenres();

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/genres`,
                undefined
            );
        });

        it('returns data on success', async () => {
            const mockData = { genres: [{ genreId: 'ROMANCE', name: 'Romance' }] };
            mockFetch.mockReturnValueOnce(mockSuccess(mockData));

            const result = await apiClient.getGenres();

            expect(result).toEqual(mockData);
        });

    });

    describe('getMyLists', () => {

        it('calls the correct URL with X-User-Id', async () => {
            mockFetch.mockReturnValueOnce(mockSuccess({ lists: [] }));

            await apiClient.getMyLists();

            expect(mockFetch).toHaveBeenCalledWith(
                `${BASE_URL}/my-lists/`,
                expect.objectContaining({
                    headers: expect.objectContaining({
                        'X-User-Id': USER_ID
                    })
                })
            );
        });

        it('throws error on failure', async () => {
            mockFetch.mockReturnValueOnce(mockError(400));

            await expect(apiClient.getMyLists()).rejects.toThrow('400');
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
                apiClient.getLibraryItem('http://localhost:8081/my-library/INVALID')
            ).rejects.toThrow('404');
        });

    });

    describe('getByHref', () => {

        it('calls provided href with X-User-Id header', async () => {
            const href =
                'http://localhost:8081/my-library/ITEM-001';

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
                'http://localhost:8081/my-library/ITEM-001'
            );

            expect(result).toEqual(data);
        });

        it('throws error on failure', async () => {
            mockFetch.mockReturnValueOnce(
                mockError(404)
            );

            await expect(
                apiClient.getByHref(
                    'http://localhost:8081/my-library/INVALID'
                )
            ).rejects.toThrow('404');
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

});