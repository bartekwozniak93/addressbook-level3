package seedu.addressbook.commands;

import seedu.addressbook.common.Messages;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.*;
import seedu.addressbook.data.person.UniquePersonList.PersonNotFoundException;
import seedu.addressbook.data.tag.Tag;
import static seedu.addressbook.common.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import seedu.addressbook.data.tag.UniqueTagList;

import java.util.HashSet;
import java.util.Set;

/**
 * Edits details of the person identified using the index.
 */
public class EditCommand extends Command {

	public static final String COMMAND_WORD = "edit";

	public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
			+ "Edits details of the person identified using the index."
			+ "Contact details can be marked private by prepending 'p' to the prefix.\n\t"
			+ "Parameters: INDEX NAME [p]p/PHONE [p]e/EMAIL [p]a/ADDRESS  [t/TAG]...\n\t" + "Example: " + COMMAND_WORD
			+ " 1 John Doe p/98765432 e/johnd@gmail.com a/311, Clementi Ave 2, #02-25 t/friends t/owesMoney";

	public static final String MESSAGE_SUCCESS = "Details of the person edited: %1$s";

	private final Person toAdd;

	/**
	 * Convenience constructor using raw values.
	 *
	 * @throws IllegalValueException
	 *             if any of the raw values are invalid
	 */
	public EditCommand(int targetVisibleIndex, String name, String phone, boolean isPhonePrivate, String email,
			boolean isEmailPrivate, String address, boolean isAddressPrivate, Set<String> tags)
			throws IllegalValueException {
		super(targetVisibleIndex);
		final Set<Tag> tagSet = new HashSet<>();
		for (String tagName : tags) {
			tagSet.add(new Tag(tagName));
		}
		this.toAdd = new Person(new Name(name), new Phone(phone, isPhonePrivate), new Email(email, isEmailPrivate),
				new Address(address, isAddressPrivate), new UniqueTagList(tagSet));
	}

	@Override
	public CommandResult execute() {
		try {
			ReadOnlyPerson target = getTargetPerson();
			addressBook.addPerson(toAdd);
			addressBook.removePerson(target);
			return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
		} catch (IndexOutOfBoundsException ie) {
			return new CommandResult(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
		} catch (PersonNotFoundException pnfe) {
			return new CommandResult(Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK);
		} catch (IllegalValueException e) {
			return new CommandResult(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
		}

	}

}
