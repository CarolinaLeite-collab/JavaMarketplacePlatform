import { render, screen } from '@/test-utils';
import userEvent from '@testing-library/user-event';
import { vi } from 'vitest';
import { AddItemModal } from '@/components/addItemModal/AddItemModal';

describe('AddItemModal', () => {
    it('calls onItemAdded with the created item', async () => {
        const user = userEvent.setup({ delay: null });

        const onItemAdded = vi.fn();
        const onClose = vi.fn();

        render(
            <AddItemModal
                opened={true}
                onClose={onClose}
                onItemAdded={onItemAdded}
            />
        );

        await user.type(
            screen.getByLabelText(/title/i),
            'Clean Code'
        );

        await user.type(
            screen.getByLabelText(/author/i),
            'Robert C. Martin'
        );

        await user.type(
            screen.getByLabelText(/release year/i),
            '2008'
        );

        await user.click(
            screen.getByRole('combobox', { name: /genre/i })
        );

        await user.click(
            screen.getByText('Other')
        );

        await user.click(
            screen.getByRole('button', { name: /next/i })
        );

        await user.type(
            await screen.findByLabelText(/publication type/i),
            'Book'
        );

        await user.type(
            screen.getByLabelText(/isbn\/issn/i),
            '9780132350884'
        );

        await user.type(
            screen.getByLabelText(/language/i),
            'English'
        );

        await user.type(
            screen.getByLabelText(/publishing company/i),
            'Prentice Hall'
        );

        await user.type(
            screen.getByLabelText(/publishing year/i),
            '2008'
        );

        await user.type(
            screen.getByLabelText(/weight/i),
            '800g'
        );

        await user.type(
            screen.getByLabelText(/dimension/i),
            '23 x 18 cm'
        );

        await user.type(
            screen.getByLabelText(/binding/i),
            'Paperback'
        );

        await user.type(
            screen.getByLabelText(/number of pages/i),
            '464'
        );

        await user.type(
            screen.getByLabelText(/edition number/i),
            '1'
        );

        await user.click(
            screen.getByRole('button', { name: /next/i })
        );

        await user.click(
            screen.getByRole('combobox', { name: /condition/i })
        );

        await user.click(
            screen.getByText('GOOD')
        );

        await user.type(
            screen.getByLabelText(/description/i),
            'Used copy in good condition'
        );

        await user.type(
            screen.getByLabelText(/picture url/i),
            'https://example.com/clean-code.jpg'
        );

        await user.click(
            screen.getByRole('button', { name: /create item/i })
        );

        await user.click(
            await screen.findByRole('button', { name: /add to library/i })
        );

        expect(onItemAdded).toHaveBeenCalledWith(
            expect.objectContaining({
                title: 'Clean Code',
                authorName: 'Robert C. Martin',
                identifier: '9780132350884',
                publicationType: 'Book',
            })
        );
    }, 15000);
});