import { describe, it, expect, vi } from 'vitest';
import userEvent from '@testing-library/user-event';
import { render, screen } from '../test-utils';
import { PlaceBidModal } from '../components/placeBidModal/PlaceBidModal.tsx';

describe('PlaceBidModal', () => {
    const defaultProps = {
        opened: true,
        currentPrice: 50,
        currency: 'EUR',
        onClose: vi.fn(),
        onConfirm: vi.fn(),
    };

    it('shows current bid and currency', () => {
        render(<PlaceBidModal {...defaultProps} />);

        expect(
            screen.getByText(/current bid:\s*50\s*eur/i)
        ).toBeInTheDocument();
    });

    it('calls onClose when X is clicked', async () => {
        const user = userEvent.setup();
        const onClose = vi.fn();

        render(
            <PlaceBidModal
                {...defaultProps}
                onClose={onClose}
            />
        );

        const closeButton = screen.getByRole('button', { name: /close/i });
        await user.click(closeButton);

        expect(onClose).toHaveBeenCalled();
    });

    it('shows error if bid is empty or non‑positive', async () => {
        const user = userEvent.setup();

        render(<PlaceBidModal {...defaultProps} />);

        // submit without typing anything
        await user.click(screen.getByRole('button', { name: /confirm bid/i }));

        expect(
            screen.getByText(/please enter a positive amount/i)
        ).toBeInTheDocument();
    });

    it('shows error if bid is not greater than current price', async () => {
        const user = userEvent.setup();

        render(<PlaceBidModal {...defaultProps} />);

        const input = screen.getByLabelText(/your bid/i);
        await user.clear(input);
        await user.type(input, '50'); // equal to currentPrice

        await user.click(screen.getByRole('button', { name: /confirm bid/i }));

        expect(
            screen.getByText(/must be greater than 50 eur/i)
        ).toBeInTheDocument();
    });

    it('calls onConfirm with valid bid', async () => {
        const user = userEvent.setup();
        const onConfirm = vi.fn();

        render(
            <PlaceBidModal
                {...defaultProps}
                onConfirm={onConfirm}
            />
        );

        const input = screen.getByLabelText(/your bid/i);
        await user.clear(input);
        await user.type(input, '60');

        await user.click(screen.getByRole('button', { name: /confirm bid/i }));

        expect(onConfirm).toHaveBeenCalledWith(60);
    });
});