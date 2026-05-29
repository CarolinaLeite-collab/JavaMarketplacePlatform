import { render, screen, fireEvent } from '@/test-utils';
import { ItemAccordion } from '../components/accordion/ItemAccordion';

const mockItems = [{
    itemId: 'ITM-001',
    title: 'The War of the Worlds',
    imageUrl: '/book-cover.jpg',
    publicationType: 'Book',
    authorName: 'H.G. Wells',
    identifier: '9781784872113'
},
{
    itemId: 'ITM-002',
    title: 'Duna',
    imageUrl: null,
    publicationType: 'Book',
    authorName: 'Frank Herbert',
    identifier: '9780593099322'
}];

describe('ItemAccordion', () => {

    it('shows all the titles in the list', () => {
        render(<ItemAccordion items={mockItems} />);

        expect(screen.getByText('The War of the Worlds')).toBeInTheDocument();
        expect(screen.getByText('Duna')).toBeInTheDocument();
    });

    it('does not show the details before clicking', () => {
        render(<ItemAccordion item={mockItem} />);

        expect(screen.queryByText('Type')).not.toBeInTheDocument();
        expect(screen.queryByText('Author')).not.toBeInTheDocument();
        expect(screen.queryByText('ISBN')).not.toBeInTheDocument();
    });

    it('shows the details of the item selected', () => {
        render(<ItemAccordion items={mockItems} />);

        fireEvent.click(screen.getByText('The War of the Worlds'));

        expect(screen.getByText('H.G. Wells')).toBeInTheDocument();
        expect(screen.getByText('9781784872113')).toBeInTheDocument();
    });

    it('render without errors when the list is empty', () => {
        render(<LibraryAccordion items={[]} />);

        expect(screen.queryByText('Type')).not.toBeInTheDocument();
    });

    it('shows the image with the correct alt', () => {
        render(<ItemAccordion item={mockItem} />);

        const img = screen.getByAltText('The War of the Worlds');
        expect(img).toBeInTheDocument();
    });

    it('uses placeholder when imageUrl is null', () => {
        render(<ItemAccordion item={{ ...mockItem, imageUrl: null }} />);

        const img = screen.getByAltText('The War of the Worlds');
        expect(img).toBeInTheDocument();
    });

});