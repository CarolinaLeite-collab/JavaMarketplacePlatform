import { render, screen } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { MantineProvider } from '@mantine/core';
import { describe, expect, it, beforeEach, vi } from 'vitest';
import { useUser } from '../context/UserContext';
import AuctionDetailPage from '../pages/AuctionDetail/AuctionDetailPage';
import { apiClient } from '../services/apiClient';

vi.mock('../context/UserContext', async () => {
    const actual = await vi.importActual('../context/UserContext');
    return { ...actual, useUser: vi.fn() };
});

vi.mock('../services/apiClient', () => ({
    apiClient: {
        getAuctionById: vi.fn(),
        getItemById: vi.fn(),
        getEditionById: vi.fn(),
        getPublishingCompanyById: vi.fn(),
        postByHref: vi.fn(),
    },
}));

const mockAuction = {
    auctionId: 'test-123',
    itemIds: ['ITEM-001'],
    startingPrice: 20.0,
    reservePrice: 30.0,
    outrightPrice: 50.0,
    priceCurrency: 'EUR',
    startDate: '2026-06-01T00:00:00Z',
    endDate: '2026-06-25T00:00:00Z',
    bidCount: 3,
    highestBid: 28.5,
    _links: { placeBid: { href: 'http://localhost:8081/auctions/test-123/bids' } },
};

const mockItem = {
    itemId: 'ITEM-001',
    editionId: 'ED-001',
    condition: 'GOOD',
    description: 'Paperback in good condition. Essential reading on urban density.',
    saleStatus: 'NotOnSale',
    picture: 'https://example.com/cover.jpg',
    identifier: '9780471989677',
    language: 'ENGLISH',
    publishingYear: 2005,
    publicationTypeName: 'BOOK',
    title: 'Spacematrix: Space, Density and Urban Form',
    authorName: 'Meta Berghauser Pont',
    releaseYear: 2005,
    genreName: 'Arts',
};

const mockEdition = {
    editionId: 'ED-001',
    publishingCompanyId: 'PC-001',
    numberOfPages: 320,
    editionNumber: 1,
    binding: 'PAPERBACK',
    weight: { value: 794, unit: 'GRAMS' },
    dimension: { width: 17.0, height: 24.0, depth: 2.5, unit: 'CENTIMETERS' },
};

const mockPublisher = {
    publishingCompanyId: 'PC-001',
    publishingCompanyName: 'naiOIO Publishers',
};

function renderAuctionDetail({ auctionId = 'test-123' } = {}) {
    return render(
        <MantineProvider>
            <MemoryRouter initialEntries={[`/auctions/${auctionId}`]}>
                <Routes>
                    <Route path="/auctions/:auctionId" element={<AuctionDetailPage />} />
                </Routes>
            </MemoryRouter>
        </MantineProvider>
    );
}

describe('AuctionDetailPage', () => {
    beforeEach(() => {
        vi.clearAllMocks();
        vi.mocked(useUser).mockReturnValue({
            currentUser: 'pedro@aeiou.com',
            toggleUser: vi.fn(),
        });
        vi.mocked(apiClient.getAuctionById).mockResolvedValue(mockAuction);
        vi.mocked(apiClient.getItemById).mockResolvedValue(mockItem);
        vi.mocked(apiClient.getEditionById).mockResolvedValue(mockEdition);
        vi.mocked(apiClient.getPublishingCompanyById).mockResolvedValue(mockPublisher);
    });

    it('renders publication type and title', async () => {
        renderAuctionDetail();

        const result = await screen.findByText(/Book:/);
        expect(result).toBeInTheDocument();
        expect(screen.getByText('Spacematrix: Space, Density and Urban Form')).toBeInTheDocument();
    });

    it('renders highest bid when bids exist', async () => {
        renderAuctionDetail();

        const result = await screen.findByText(/28.5 EUR/);
        expect(result).toBeInTheDocument();
    });

    it('renders starting price when no bids', async () => {
        vi.mocked(apiClient.getAuctionById).mockResolvedValue({
            ...mockAuction,
            highestBid: null,
            bidCount: 0,
        });
        renderAuctionDetail();

        const results = await screen.findAllByText(/20 EUR/);
        expect(results.length).toBeGreaterThanOrEqual(1);
    });

    it('renders starting price label', async () => {
        renderAuctionDetail();

        const result = await screen.findByText(/Starting price: 20 EUR/);
        expect(result).toBeInTheDocument();
    });

    it('renders bid count button', async () => {
        renderAuctionDetail();

        const result = await screen.findByRole('button', { name: /3 bids/ });
        expect(result).toBeInTheDocument();
    });

    it('renders seller', async () => {
        renderAuctionDetail();

        const result = await screen.findByText(/Sold by Unknown/);
        expect(result).toBeInTheDocument();
    });

    it('renders status badge', async () => {
        renderAuctionDetail();

        const result = await screen.findByText('Active');
        expect(result).toBeInTheDocument();
    });

    it('renders seller description', async () => {
        renderAuctionDetail();

        expect(await screen.findByText("Seller's description:")).toBeInTheDocument();
        expect(screen.getByText(/Essential reading on urban density/)).toBeInTheDocument();
    });

    it('renders quick info cards', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Author')).toBeInTheDocument();
        expect(screen.getByText('Meta Berghauser Pont')).toBeInTheDocument();
        expect(screen.getByText('Edition')).toBeInTheDocument();
        expect(screen.getByText('1')).toBeInTheDocument();
        expect(screen.getByText('ISBN')).toBeInTheDocument();
        expect(screen.getByText('9780471989677')).toBeInTheDocument();
        expect(screen.getByText('Condition')).toBeInTheDocument();
        expect(screen.getByText('GOOD')).toBeInTheDocument();
    });

    it('renders details table with publisher', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Publisher')).toBeInTheDocument();
        expect(screen.getByText('naiOIO Publishers')).toBeInTheDocument();
    });

    it('renders details table with genre', async () => {
        renderAuctionDetail();

        const genreLabels = await screen.findAllByText('Genre');
        expect(genreLabels.length).toBeGreaterThanOrEqual(1);
        expect(screen.getByText('Arts')).toBeInTheDocument();
    });

    it('renders details table with year and language', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Year')).toBeInTheDocument();
        expect(screen.getByText('2005')).toBeInTheDocument();
        expect(screen.getByText('ENGLISH')).toBeInTheDocument();
    });

    it('renders details table with pages and binding', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Pages')).toBeInTheDocument();
        expect(screen.getByText('320')).toBeInTheDocument();
        expect(screen.getByText('PAPERBACK')).toBeInTheDocument();
    });

    it('renders details table with weight', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Weight')).toBeInTheDocument();
        expect(screen.getByText('794 GRAMS')).toBeInTheDocument();
    });

    it('renders details table with dimensions', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Dimensions')).toBeInTheDocument();
        expect(screen.getByText('17 x 24 x 2.5 CENTIMETERS')).toBeInTheDocument();
    });

    it('renders Place Bid button for logged-in user', async () => {
        renderAuctionDetail();

        const result = await screen.findByRole('button', { name: /place bid/i });
        expect(result).toBeInTheDocument();
        expect(result).not.toBeDisabled();
    });

    it('disables Place Bid for guest user', async () => {
        vi.mocked(useUser).mockReturnValue({
            currentUser: 'guest@aeiou.com',
            toggleUser: vi.fn(),
        });
        renderAuctionDetail();

        const result = await screen.findByRole('button', { name: /place bid/i });
        expect(result).toBeDisabled();
    });

    it('renders Buy Now and Add to Cart buttons', async () => {
        renderAuctionDetail();

        expect(await screen.findByRole('button', { name: /buy now/i })).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /add to cart/i })).toBeInTheDocument();
    });

    it('shows N/A for edition fields when edition fails to load', async () => {
        vi.mocked(apiClient.getEditionById).mockRejectedValue(new Error('404'));
        renderAuctionDetail();

        expect(await screen.findByText('Publisher')).toBeInTheDocument();
        const naCells = screen.getAllByText('N/A');
        expect(naCells.length).toBeGreaterThanOrEqual(1);
    });
});