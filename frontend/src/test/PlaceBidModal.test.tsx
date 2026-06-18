import { describe, it, expect, vi, beforeEach } from 'vitest';
import userEvent from '@testing-library/user-event';
import { render, screen } from '../test-utils';
import { PlaceBidModal } from '../components/placeBidModal/PlaceBidModal.tsx';

describe('PlaceBidModal', () => {
    let defaultProps: {
        opened: boolean;
        currentPrice: number;
        currency: string;
        onClose: ReturnType<typeof vi.fn>;
        onConfirm: ReturnType<typeof vi.fn>;
    };

    beforeEach(() => {
        defaultProps = {
            opened: true,
            currentPrice: 50,
            currency: 'EUR',
            onClose: vi.fn(),
            onConfirm: vi.fn(),
        };
    });

    it('shows current bid and currency', () => {
        render(<PlaceBidModal {...defaultProps} />);

        expect(screen.getByText(/current bid:\s*50\s*eur/i)).toBeInTheDocument();
    });

    it('calls onClose when X is clicked', async () => {
        const user = userEvent.setup();
        const onClose = vi.fn();

        render(<PlaceBidModal {...defaultProps} onClose={onClose} />);

        await user.click(screen.getByRole('button', { name: /close/i }));

        expect(onClose).toHaveBeenCalledTimes(1);
    });

    it('shows error if bid is empty', async () => {
        const user = userEvent.setup();

        render(<PlaceBidModal {...defaultProps} />);

        await user.click(screen.getByRole('button', { name: /confirm bid/i }));

        expect(screen.getByText(/please enter a positive amount/i)).toBeInTheDocument();
    });

    it('shows error if bid is not greater than current price', async () => {
        const user = userEvent.setup();

        render(<PlaceBidModal {...defaultProps} />);

        const input = screen.getByLabelText(/your bid/i);
        await user.clear(input);
        await user.type(input, '50');

        await user.click(screen.getByRole('button', { name: /confirm bid/i }));

        expect(screen.getByText(/your bid must be greater than 50 eur\./i)).toBeInTheDocument();
    });

    it('calls onConfirm with valid bid', async () => {
        const user = userEvent.setup();
        const onConfirm = vi.fn();

        render(<PlaceBidModal {...defaultProps} onConfirm={onConfirm} />);

        const input = screen.getByLabelText(/your bid/i);
        await user.clear(input);
        await user.type(input, '60');

        await user.click(screen.getByRole('button', { name: /confirm bid/i }));

        expect(onConfirm).toHaveBeenCalledTimes(1);
        expect(onConfirm).toHaveBeenCalledWith(60);
    });

    it('sets value with preset increment buttons', async () => {
        const user = userEvent.setup();

        render(<PlaceBidModal {...defaultProps} />);

        await user.click(screen.getByRole('button', { name: /\+10 eur/i }));

        const input = screen.getByLabelText(/your bid/i);
        expect(input).toHaveValue('60 EUR');
    });

    it('calls onClose when cancel is clicked', async () => {
        const user = userEvent.setup();
        const onClose = vi.fn();

        render(<PlaceBidModal {...defaultProps} onClose={onClose} />);

        await user.click(screen.getByRole('button', { name: /cancel/i }));

        expect(onClose).toHaveBeenCalledTimes(1);
    });
});