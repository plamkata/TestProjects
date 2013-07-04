package indexer.builder;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class SampleNature implements IProjectNature {

	/**
	 * ID of this project nature
	 */
	public static final String NATURE_ID = "indexer.sampleNature";

	private IProject project;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IProjectNature#configure()
	 */
	public void configure() throws CoreException {
		IProjectDescription desc = project.getDescription();
		ICommand[] commands = desc.getBuildSpec();

		int index = -1;
		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(SampleBuilder.BUILDER_ID)) {
				index = i;
				break;
			}
		}

		if (index == -1) {
			ICommand[] newCommands = new ICommand[commands.length + 1];
			System.arraycopy(commands, 0, newCommands, 0, commands.length);
			
			ICommand command = desc.newCommand();
			command.setBuilderName(SampleBuilder.BUILDER_ID);
			
			newCommands[newCommands.length - 1] = command;
			
			desc.setBuildSpec(newCommands);
			project.setDescription(desc, null);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IProjectNature#deconfigure()
	 */
	public void deconfigure() throws CoreException {
		IProjectDescription description = getProject().getDescription();
		ICommand[] commands = description.getBuildSpec();
		
		int index = -1;
		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(SampleBuilder.BUILDER_ID)) {
				index = i;			
				break;
			}
		}
		
		if (index != -1) {
			ICommand[] newCommands = new ICommand[commands.length - 1];
			System.arraycopy(commands, 0, newCommands, 0, index);
			System.arraycopy(commands, index + 1, newCommands, index,
					commands.length - index - 1);
			description.setBuildSpec(newCommands);
		}	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IProjectNature#getProject()
	 */
	public IProject getProject() {
		return project;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IProjectNature#setProject(org.eclipse.core.resources.IProject)
	 */
	public void setProject(IProject project) {
		this.project = project;
	}

}
