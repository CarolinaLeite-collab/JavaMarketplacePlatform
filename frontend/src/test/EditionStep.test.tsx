import {render, screen} from '@/test-utils';
import userEvent from '@testing-library/user-event';
import {EditionStep} from '../components/addItemModal/EditionStep';

describe('EditionStep', () => {
    const data = {
        identifier: '',
        publicationType: '',
        editionLanguage: '',
        publishingCompany: '',
        publishingYear: 0,
        weight: '',
        dimension: '',
        binding: '',
        numberOfPages: 0,
        editionNumber: 0,
    };

    it('renders all edition fields', () => {
        render(<EditionStep data={data} setData={vi.fn()} />);

        expect(screen.getByLabelText(/publication type/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/isbn\/issn/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/language/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/publishing company/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/publishing year/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/weight/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/dimension/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/binding/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/number of pages/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/edition number/i)).toBeInTheDocument();
    });

    it('updates text fields when user types', async () => {
        const user = userEvent.setup();
        const setData = vi.fn();

        render(<EditionStep data={data} setData={setData} />);

        await user.type(screen.getByLabelText(/publication type/i), 'Book');

        expect(setData).toHaveBeenCalled();
    });

    it('updates number fields when user types', async () => {
        const user = userEvent.setup();
        const setData = vi.fn();

        render(<EditionStep data={data} setData={setData} />);

        await user.type(screen.getByLabelText(/publishing year/i), '2008');

        expect(setData).toHaveBeenCalled();
    });
});