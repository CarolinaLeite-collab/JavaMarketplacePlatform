import { render, screen } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { MantineProvider } from '@mantine/core';
import { describe, expect, it, beforeEach, vi } from 'vitest';
import { useUser } from '../context/UserContext';
import AuctionDetailPage from '../pages/AuctionDetail/AuctionDetailPage';

vi.mock('../context/UserContext', async () => {
    const actual = await vi.importActual('../context/UserContext');
    return {
        ...actual,
        useUser: vi.fn(),
    };
});

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
    });

    it('renders auction title', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('1984')).toBeInTheDocument();
    });

    it('renders highest bid in large text', async () => {
        renderAuctionDetail();

        expect(await screen.findByText(/75/)).toBeInTheDocument();
    });

    it('renders starting price', async () => {
        renderAuctionDetail();

        expect(await screen.findByText(/Starting price: 50 EUR/)).toBeInTheDocument();
    });

    it('renders number of bids', async () => {
        renderAuctionDetail();

        expect(await screen.findByText(/6 bids/)).toBeInTheDocument();
    });

    it('renders seller', async () => {
        renderAuctionDetail();

        expect(await screen.findByText(/Sold by Unknown/)).toBeInTheDocument();
    });

    it('renders deadline', async () => {
        renderAuctionDetail();

        expect(await screen.findByText(/3 days, 2025-07-15/)).toBeInTheDocument();
    });

    it('renders status badge', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Active')).toBeInTheDocument();
    });

    it('renders description card', async () => {
        renderAuctionDetail();

        expect(await screen.findByText(/First edition of George Orwell/)).toBeInTheDocument();
    });

    it('renders synopsis section', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('SYNOPSIS:')).toBeInTheDocument();
        expect(screen.getByText(/seminal texts/)).toBeInTheDocument();
    });

    it('renders details table with author and publisher', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Author')).toBeInTheDocument();
        expect(screen.getByText('George Orwell')).toBeInTheDocument();
        expect(screen.getByText('Publisher')).toBeInTheDocument();
        expect(screen.getByText('Secker & Warburg')).toBeInTheDocument();
    });

    it('renders details table with genre and edition', async () => {
        renderAuctionDetail();

        const genreLabels = await screen.findAllByText('Genre');
        expect(genreLabels.length).toBeGreaterThanOrEqual(1);
        expect(screen.getByText('1st Edition')).toBeInTheDocument();
    });

    it('renders details table with publication type and identifier', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Publication type')).toBeInTheDocument();
        expect(screen.getByText('Book')).toBeInTheDocument();
        expect(screen.getByText('978-0-451-52493-5')).toBeInTheDocument();
    });

    it('renders details table with year and language', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Year')).toBeInTheDocument();
        expect(screen.getByText('1949')).toBeInTheDocument();
        expect(screen.getByText('English')).toBeInTheDocument();
    });

    it('renders details table with condition and weight', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Condition')).toBeInTheDocument();
        expect(screen.getByText('Good')).toBeInTheDocument();
        expect(screen.getByText('350 g')).toBeInTheDocument();
    });

    it('renders details table with dimensions', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Dimensions')).toBeInTheDocument();
        expect(screen.getByText('20 x 13 x 2.5 cm')).toBeInTheDocument();
    });

    it('renders Place Bid button', async () => {
        renderAuctionDetail();

        expect(await screen.findByRole('button', { name: /place bid/i })).toBeInTheDocument();
    });

    it('renders Buy Now button', async () => {
        renderAuctionDetail();

        expect(await screen.findByRole('button', { name: /buy now/i })).toBeInTheDocument();
    });

    it('renders Add to Cart button', async () => {
        renderAuctionDetail();

        expect(await screen.findByRole('button', { name: /add to cart/i })).toBeInTheDocument();
    });
});