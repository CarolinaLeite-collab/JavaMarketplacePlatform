import { render, screen } from '@/test-utils';
import userEvent from '@testing-library/user-event';
import { CreateSaleModal } from '../components/createSaleModal/CreateSaleModal';
import {waitFor} from "@testing-library/react";

describe('CreateSaleModal', () => {
    it('renders create sale button', () => {
        render(<CreateSaleModal />);

        expect(
            screen.getByRole('button', { name: /create a sale/i })
        ).toBeInTheDocument();
    });

    it('opens modal when clicking the button', async () => {
        const user = userEvent.setup();
        render(<CreateSaleModal />);

        await user.click(screen.getByRole('button', { name: /create a sale/i }));

        expect(
            await screen.getByRole('heading', { name: /create new sale/i })
        ).toBeInTheDocument();
    });

    it('renders all form fields when modal opens', async () => {
        const user = userEvent.setup();
        render(<CreateSaleModal />);

        await user.click(screen.getByRole('button', { name: /create a sale/i }));

        expect(
            await screen.findByRole('heading', { name: /create new sale/i })
        ).toBeInTheDocument();
        expect(await screen.findByLabelText(/sale type/i)).toBeInTheDocument();
        expect(await screen.findByLabelText(/item/i)).toBeInTheDocument();
        expect(await screen.findByRole('textbox', { name: /price value/i })).toBeInTheDocument();
        expect(await screen.findByLabelText(/currency/i)).toBeInTheDocument();
        expect(await screen.findByRole('textbox', { name: /duration \(days\)/i })).toBeInTheDocument();
        expect(await screen.findByRole('button', { name: /create sale/i })).toBeInTheDocument();
    });

    it('closes modal when clicking create sale', async () => {
        const user = userEvent.setup();
        render(<CreateSaleModal />);

        await user.click(screen.getByRole('button', { name: /create a sale/i }));

        const createButton = await screen.findByRole('button', { name: /^create sale$/i });
        await user.click(createButton);

        await waitFor(() => {
            expect(
                screen.queryByRole('heading', { name: /create new sale/i })
            ).not.toBeInTheDocument();
        })
    });
});