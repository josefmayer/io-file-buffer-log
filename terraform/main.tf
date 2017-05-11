variable private_key_path{
  description = "Path to the SSH private key to be used for authentication"
  default = "~/.ssh/id_rsa"
}

variable public_key_path{
  description = "Path to the SSH public key to be used for authentication"
  default = "~/.ssh/id_rsa.pub"
}

variable sftp_batch_path {
  description = "Path do sftp batch file"
  default = "sftp_batchfile"
}

resource "aws_key_pair" "aws_test_1" {
  key_name   = "aws_test_1"               # key pair name AWS
  public_key = "${file(var.public_key_path)}"
}

provider "aws" {
  region = "eu-central-1"
}

#resource "aws_vpc" "default" {
#  cidr_block = "10.0.0.0/16"
#}

resource "aws_security_group" "aws_test_1" {
  name        = "aws_test_1"
  description = "Used in the terraform"
  # vpc_id      = "${aws_vpc.default.id}"

  # SSH access from anywhere
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # HTTP access from the internet
  egress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # outbound internet access
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}


resource "aws_instance" "ec2_instance_test_buffer_log" {
  # ami = "ami-060cde69"
  ami = "ami-16b26c79"   # ami created with packer: ubuntu, java

  instance_type = "t2.micro"

  key_name = "${aws_key_pair.aws_test_1.id}"

  vpc_security_group_ids = ["${aws_security_group.aws_test_1.id}"]


  connection {
    # The default username for our AMI
    user = "ubuntu"
    type = "ssh"
    private_key = "${file(var.private_key_path)}"
    # The connection will use the local SSH agent for authentication.
  }

  # install java, create dir
  provisioner "remote-exec" {
    inline = [
      # "sudo apt-get -y update",
      # "sudo apt-get -y install openjdk-8-jre-headless",
      "mkdir -p data/inbox",
    ]
  }


  # upload inbox directory content
  provisioner "file" {
    source      = "../data/inbox/"
    destination = "~/data/inbox"
  }

  # upload jar file
  provisioner "file" {
    source      = "../target/io-file-copy-1.0-SNAPSHOT.jar"
    destination = "~/io-file-copy-1.0-SNAPSHOT.jar"
  }

  # run jar
  provisioner "remote-exec" {
    inline = [
      "java -jar io-file-copy-1.0-SNAPSHOT.jar",
    ]
  }

  provisioner "local-exec" {
    command =
    "mkdir -p log_aws"
  }

  provisioner "local-exec" {
    command =
    "mkdir -p log4j2_aws"
  }

  # download logFile results
  provisioner "local-exec" {
    command =
    "sftp -b ${var.sftp_batch_path} -i ${var.private_key_path} -o StrictHostKeyChecking=no ubuntu@${aws_instance.ec2_instance_test_buffer_log.public_dns}"
  }


}



