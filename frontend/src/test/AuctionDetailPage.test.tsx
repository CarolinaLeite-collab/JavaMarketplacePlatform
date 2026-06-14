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

        expect(await screen.findByText('1984 - George Orwell - First Edition')).toBeInTheDocument();
    });

    it('renders description section', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('DESCRIPTION:')).toBeInTheDocument();
        expect(screen.getByText(/First edition of George Orwell/)).toBeInTheDocument();
    });

    it('renders details table with author', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Author')).toBeInTheDocument();
        expect(screen.getByText('George Orwell')).toBeInTheDocument();
    });

    it('renders details table with genre', async () => {
        renderAuctionDetail();

        const genreLabels = await screen.findAllByText('Genre');
        expect(genreLabels.length).toBeGreaterThanOrEqual(1);
        const fictionLabels = screen.getAllByText('Fiction');
        expect(fictionLabels.length).toBeGreaterThanOrEqual(1);
    });

    it('renders details table with genre', async () => {
        renderAuctionDetail();

        const genreLabels = await screen.findAllByText('Genre');
        expect(genreLabels.length).toBeGreaterThanOrEqual(1);
        const fictionLabels = screen.getAllByText('Fiction');
        expect(fictionLabels.length).toBeGreaterThanOrEqual(1);
    });

    it('renders starting price and highest bid', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Starting price')).toBeInTheDocument();
        expect(screen.getByText('Highest bid')).toBeInTheDocument();
    });

    it('renders deadline', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Deadline')).toBeInTheDocument();
        expect(screen.getByText('2025-07-15T23:59:59')).toBeInTheDocument();
    });

    it('renders status badge', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('Active')).toBeInTheDocument();
    });

    it('renders items table', async () => {
        renderAuctionDetail();

        expect(await screen.findByText('ITEMS:')).toBeInTheDocument();
        expect(screen.getByText('1984 - Hardcover')).toBeInTheDocument();
        expect(screen.getByText('1984 - Paperback')).toBeInTheDocument();
    });

    it('renders Place Bid button for logged-in user', async () => {
        renderAuctionDetail();

        expect(await screen.findByRole('button', { name: /place bid/i })).toBeInTheDocument();
    });

    it('renders bid input for logged-in user', async () => {
        renderAuctionDetail();

        expect(await screen.findByPlaceholderText('Enter bid value')).toBeInTheDocument();
    });

    it('hides bid actions for guest user', async () => {
        vi.mocked(useUser).mockReturnValue({
            currentUser: 'guest@aeiou.com',
            toggleUser: vi.fn(),
        });

        renderAuctionDetail();

        expect(await screen.findByText('1984 - George Orwell - First Edition')).toBeInTheDocument();
        expect(screen.queryByRole('button', { name: /place bid/i })).not.toBeInTheDocument();
        expect(screen.queryByPlaceholderText('Enter bid value')).not.toBeInTheDocument();
    });
});