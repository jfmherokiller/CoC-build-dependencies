# CoC-build-dependencies
Contains build dependencies for the Corruption-of-Champions-Mod CI build

### Contents
- Flex SDK
- Flash Player for Linux
- Vagrant file and script to update dependencies

### Requirements for updating
- Vagrant
- Virtualbox

## Usage
- **PowerShell or Command prompt are recommended on windows, Git Bash has issues with the interactive shell**
- In the repository root, run `vagrant up` to start and provision the VM
- Log in to the VM with `vagrant ssh`
- In the VM, run `sudo sh /vagrant/scripts/setup-flex.sh`, this will build the dependencies and then copy them to the `bin` directory in the repository root
- Exit the VM with `Ctrl + d` or with `exit`
- When done, destroy the VM with `vagrant destroy -f` in the repository root